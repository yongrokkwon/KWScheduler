package com.kwuniv.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * AlarmReceiver
 * - AlarmManager에 의해 호출되는 BroadcastReceiver 클래스
 * - 알람이 트리거되면 Notification을 생성하여 사용자에게 알림을 제공
 */
public class AlarmReceiver extends BroadcastReceiver {


    /**
     * onReceive
     * - 알람이 실행될 때 호출
     * - SharedPreferences 설정(알림, 진동, 소리)을 확인하고 알림(Notification)을 생성
     *
     * @param context Context 객체 (알림 생성 및 SharedPreferences 접근에 사용)
     * @param intent  Intent 객체 (알람 제목 등 추가 데이터 포함)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람 제목 가져오기
        String title = intent.getStringExtra("title");

        // SharedPreferencesManager 인스턴스 가져오기
        SharedPreferenceManager spManager = SharedPreferenceManager.getInstance(context);

        // 알림이 비활성화된 경우 로그 기록 후 종료
        if (!spManager.isNotificationsEnabled()) {
            Log.w("AlarmHelper", "Notifications are disabled.");
            return;
        }

        // 기본 알림 생성
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
            long[] vibrationPattern = {0, 500, 200, 500}; // 진동 패턴 (0ms 대기, 500ms 진동, 200ms 대기, 500ms 진동)
            builder.setVibrate(vibrationPattern);
        } else {
            builder.setVibrate(null);
        }

        // 알림 매니저를 통해 알림 표시
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
