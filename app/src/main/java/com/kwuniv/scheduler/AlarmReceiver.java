package com.kwuniv.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");

        SharedPreferenceManager spManager = SharedPreferenceManager.getInstance(context);

        if (!spManager.isNotificationsEnabled()) {
            Log.w("AlarmHelper", "Notifications are disabled.");
            return;
        }

        // 기본 알림 채널 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALARM_CHANNEL")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("알림")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // 소리 설정
        if (spManager.isSoundEnabled()) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
        } else {
            builder.setSound(null);
        }

        // 진동 설정
        if (spManager.isVibrationEnabled()) {
            long[] vibrationPattern = {0, 500, 200, 500}; // 진동 패턴
            builder.setVibrate(vibrationPattern);
        } else {
            builder.setVibrate(null);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
