package com.example.memoria;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "play_reminder";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    channelId,
                    "Напоминания об игре",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ch.setDescription("Канал для напоминаний о том, чтобы поиграть");
            nm.createNotificationChannel(ch);
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification) // замените на ваш значок
                .setContentTitle("Время игры!")
                .setContentText("Загляните в приложение и сыграйте в игру «Память»")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(1001, b.build());
    }
}
