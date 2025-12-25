package com.example.memoria;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private static final int REQ_POST_NOTIFICATIONS = 101;

    private Button btnSetReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets sb = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sb.left, sb.top, sb.right, sb.bottom);
                    return insets;
                }
        );

        // Запрос разрешения на уведомления (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.POST_NOTIFICATIONS },
                    REQ_POST_NOTIFICATIONS
            );
        }

        btnSetReminder = findViewById(R.id.btnSetReminder);
        btnSetReminder.setOnClickListener(v -> showTimePicker());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_POST_NOTIFICATIONS) {
            boolean granted = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                Toast.makeText(this,
                        "Без разрешения на уведомления напоминаний не будет",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goToProfileActivity(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
        overridePendingTransition(R.anim.slide_in_center_from_left, R.anim.slide_in_right_from_center);
    }

    public void goToMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_center_from_left, R.anim.slide_in_right_from_center);
    }

    private void showTimePicker() {
        Calendar now = Calendar.getInstance();
        boolean is24 = DateFormat.is24HourFormat(this);
        new TimePickerDialog(
                this,
                (picker, hourOfDay, minute) -> {
                    Log.d(TAG, "User picked: " + hourOfDay + ":" + minute);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                                    != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this,
                                "Невозможно установить напоминание без разрешения на уведомления",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    scheduleReminder(hourOfDay, minute);
                    String time = String.format("%02d:%02d", hourOfDay, minute);
                    Toast.makeText(this, "Напоминание установлено на " + time, Toast.LENGTH_SHORT).show();
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                is24
        ).show();
    }

    private void scheduleReminder(int hour, int minute) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am == null) return;

        // На Android 12+ можно запросить экран настроек точных алармов (не критично, просто подсказка)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !am.canScheduleExactAlarms()) {
            Intent req = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    .setPackage(getPackageName());
            if (req.resolveActivity(getPackageManager()) != null) {
                startActivity(req);
            }
        }

        // Вычисляем время в миллисекундах
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long triggerAt = cal.getTimeInMillis();
        long now = System.currentTimeMillis();
        if (triggerAt <= now) {
            triggerAt += AlarmManager.INTERVAL_DAY;
        }
        Log.d(TAG, "Scheduling alarm for: " + new Date(triggerAt));

        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Ставим будильник, оборачивая точные методы в try/catch, чтобы не падать без разрешения
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi);
            }
        } catch (SecurityException e) {
            Log.w(TAG, "Exact alarm failed, falling back to inexact", e);
            // fallback to inexact alarm
            am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi);
        }
    }
}
