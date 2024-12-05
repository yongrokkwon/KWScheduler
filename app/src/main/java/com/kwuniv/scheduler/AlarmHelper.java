package com.kwuniv.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

/**
 * AlarmHelper
 * - 알람 등록 및 관리 기능을 제공하는 클래스
 * - AlarmManager를 사용하여 알람을 등록하고, 필요한 경우 사용자에게 권한 요청을 유도
 */
public class AlarmHelper {

    private final Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    /**
     * setAlarm
     * - AlarmManager를 사용하여 지정된 시간에 알람을 등록
     * - Android 12(S) 이상에서는 정확한 알람을 사용하기 위해 권한을 확인하고 요청
     *
     * @param requestCode 알람의 고유 식별자 (hashCode 등으로 생성)
     * @param calendar    알람이 실행될 시간 (Calendar 객체)
     * @param title       알람의 제목 (Notification에 표시될 내용)
     */
    public void setAlarm(int requestCode, Calendar calendar, String title) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 알람을 처리할 BroadcastReceiver에 전달할 Intent 생성
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);

        // PendingIntent 생성 (FLAG_IMMUTABLE은 Android 12 이상에서 필수)
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            // 정확한 알람 설정 (Doze 모드에서도 실행 가능)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            // Android 12(S) 이상에서 정확한 알람 권한 확인 및 처리
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    try {
                        // 정확한 알람 설정
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } catch (SecurityException e) {
                        // 권한 부족으로 인한 예외 처리
                        Log.e("AlarmHelper", "Exact alarm permission denied", e);
                        requestExactAlarmPermission();
                    }
                } else {
                    // 정확한 알람을 사용할 수 없는 경우 로그 및 대체 처리
                    Log.w("AlarmHelper", "Exact alarms are not allowed for this app.");
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            } else {
                // Android 12 미만에서는 정확한 알람 바로 설정
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    /**
     * requestExactAlarmPermission
     * - Android 12(S) 이상에서 정확한 알람 권한을 요청
     * - 사용자가 권한을 수동으로 설정할 수 있도록 시스템 설정 화면으로 이동
     */
    public void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // 정확한 알람 권한 요청을 위한 설정 화면으로 이동
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
            }
        }
    }
}
