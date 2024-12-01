package com.kwuniv.scheduler;

import org.json.JSONException;
import org.json.JSONObject;

public class Alarm {

    private String title;
    private String date;
    private String time;
    private String repeat;

    public Alarm(String title, String date, String time, String repeat) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
    }

    // Constructor to create Alarm from JSONObject
    public Alarm(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("title");
        this.date = jsonObject.getString("date");
        this.time = jsonObject.getString("time");
        this.repeat = jsonObject.getString("repeat");
    }

    // Convert Alarm object to JSONObject
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("date", date);
        jsonObject.put("time", time);
        jsonObject.put("repeat", repeat);
        return jsonObject;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRepeat() {
        return repeat;
    }

    // Setters (if needed)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
