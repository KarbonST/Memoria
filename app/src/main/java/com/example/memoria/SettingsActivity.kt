package com.example.memoria

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.memoria.ui.screens.SettingsScreen
import com.example.memoria.ui.theme.MemoriaTheme
import java.util.Calendar

/**
 * Settings activity - reminder configuration.
 * Converted from Java to Kotlin with Jetpack Compose.
 * Keeps all original functionality including permission handling and alarm scheduling.
 */
class SettingsActivity : ComponentActivity() {
    private val TAG = "SettingsActivity"
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                this,
                "Без разрешения на уведомления напоминаний не будет",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        setContent {
            MemoriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen(
                        onHomeClick = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.slide_in_center_from_left,
                                R.anim.slide_in_right_from_center
                            )
                        },
                        onProfileClick = {
                            val intent = Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.slide_in_center_from_left,
                                R.anim.slide_in_right_from_center
                            )
                        },
                        onScheduleReminder = { hour, minute ->
                            // Check permission before scheduling
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED
                            ) {
                                Toast.makeText(
                                    this,
                                    "Невозможно установить напоминание без разрешения на уведомления",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@SettingsScreen
                            }
                            scheduleReminder(hour, minute)
                            val time = String.format("%02d:%02d", hour, minute)
                            Toast.makeText(this, "Напоминание установлено на $time", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
    
    // Keep the original scheduleReminder method for use by SettingsScreen
    fun scheduleReminder(hour: Int, minute: Int) {
        val am = getSystemService(ALARM_SERVICE) as? AlarmManager ?: return
        
        // Request exact alarm permission for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            val req = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                .setPackage(packageName)
            if (req.resolveActivity(packageManager) != null) {
                startActivity(req)
            }
        }
        
        // Calculate trigger time
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        
        var triggerAt = cal.timeInMillis
        val now = System.currentTimeMillis()
        if (triggerAt <= now) {
            triggerAt += AlarmManager.INTERVAL_DAY
        }
        Log.d(TAG, "Scheduling alarm for: ${java.util.Date(triggerAt)}")
        
        val intent = Intent(this, ReminderReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Schedule alarm with fallback
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi)
            } else {
                am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "Exact alarm failed, falling back to inexact", e)
            am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        }
    }
}

