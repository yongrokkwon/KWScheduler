package com.kwuniv.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> dates;
    private final HashMap<String, List<String>> scheduleMap;

    public CalendarAdapter(Context context, List<String> dates, HashMap<String, List<String>> scheduleMap) {
        this.context = context;
        this.dates = dates;
        this.scheduleMap = scheduleMap;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar_date, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView scheduleTextView = convertView.findViewById(R.id.scheduleTextView);

        String date = dates.get(position);
        dateTextView.setText(date);

        // Fetch schedules for the date
        List<String> schedules = scheduleMap.getOrDefault(date, null);

        if (schedules == null || schedules.isEmpty()) {
            scheduleTextView.setText(""); // No schedules
        } else if (schedules.size() <= 2) {
            // Display up to 2 schedules
            scheduleTextView.setText(String.join("\n", schedules));
        } else {
            // If more than 2 schedules, display "+N개"
            scheduleTextView.setText(schedules.get(0) + "\n+ " + (schedules.size() - 1) + "개");
        }

        return convertView;
    }

    public void updateDates(List<String> newDates) {
        dates.clear();
        dates.addAll(newDates);
        notifyDataSetChanged();
    }
}
