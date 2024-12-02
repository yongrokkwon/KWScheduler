package com.kwuniv.scheduler;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SharedPreferenceManager {

    private static final String PREF_NAME = "AppSharedPreferences";
    private static final String KEY_ALARMS = "alarms";

    private static SharedPreferenceManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
