package com.kwuniv.scheduler;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceManager {

    private static final String PREF_NAME = "AppSharedPreferences";
    private static final String KEY_ALARMS = "alarms";

    private static final String KEY_NOTIFICATIONS_ENABLED = "notification_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";

    private static SharedPreferenceManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 알림 설정 저장 및 확인
    public boolean isNotificationsEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true); // 기본값: true
    }

    public void setNotificationsEnabled(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, isEnabled).apply();
    }

    // 진동 설정 저장 및 불러오기
    public boolean isVibrationEnabled() {
        return sharedPreferences.getBoolean(KEY_VIBRATION_ENABLED, false);
    }

    public void setVibrationEnabled(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_VIBRATION_ENABLED, isEnabled).apply();
    }

    // 소리 설정 저장 및 불러오기
    public boolean isSoundEnabled() {
        return sharedPreferences.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_SOUND_ENABLED, isEnabled).apply();
    }

    // Singleton instance
    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    // Save a single alarm
    public void saveAlarm(Alarm alarm) {
        try {
            JSONArray alarms = getAlarmsArray();
            alarms.put(alarm.toJsonObject());
            saveAlarmsArray(alarms);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Get list of alarms
    public List<Alarm> getAlarms() {
        List<Alarm> alarmList = new ArrayList<>();
        try {
            JSONArray alarms = getAlarmsArray();
            for (int i = 0; i < alarms.length(); i++) {
                alarmList.add(new Alarm(alarms.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alarmList;
    }

    public List<Alarm> getAlarmsByMonth(int year, int month) {
        List<Alarm> alarmList = new ArrayList<>();
        try {
            JSONArray alarms = getAlarmsArray();
            for (int i = 0; i < alarms.length(); i++) {
                JSONObject alarmJson = alarms.getJSONObject(i);
                Alarm alarm = new Alarm(alarmJson);

                // Parse date to check year and month
                String[] dateParts = alarm.getDate().split("-");
                int alarmYear = Integer.parseInt(dateParts[0]);
                int alarmMonth = Integer.parseInt(dateParts[1]);

                if (alarmYear == year && alarmMonth == month) {
                    alarmList.add(alarm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alarmList;
    }


    // Delete an alarm by index
    public void deleteAlarm(int index) {
        JSONArray alarms = getAlarmsArray();
        if (index >= 0 && index < alarms.length()) {
            alarms.remove(index);
            saveAlarmsArray(alarms);
        }
    }

    // Clear all alarms
    public void clearAlarms() {
        saveAlarmsArray(new JSONArray());
    }

    // Helper: Get the alarms as JSONArray
    private JSONArray getAlarmsArray() {
        String alarmsJson = sharedPreferences.getString(KEY_ALARMS, "[]");
        try {
            return new JSONArray(alarmsJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // Helper: Save the alarms JSONArray to SharedPreferences
    private void saveAlarmsArray(JSONArray alarms) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ALARMS, alarms.toString());
        editor.apply();
    }
}
