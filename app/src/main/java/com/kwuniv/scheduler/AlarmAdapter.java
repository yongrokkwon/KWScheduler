package com.kwuniv.scheduler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private final List<Alarm> alarms;

    public AlarmAdapter(List<Alarm> alarms) {
        this.alarms = alarms != null ? alarms : new ArrayList<>();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);

        // Set data
        holder.title.setText(alarm.getTitle());
        holder.dateTime.setText(String.format("%s %s", alarm.getDate(), alarm.getTime()));
        holder.repeat.setText(String.format("반복: %s", alarm.getRepeat()));
    }

    @Override
    public int getItemCount() {
        Log.d("###### getItemCount", alarms.size() + "");
        return alarms.size();
    }

    public void updateAlarms(List<Alarm> newAlarms) {
        alarms.clear();
        alarms.addAll(newAlarms);
        notifyDataSetChanged();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView title, dateTime, repeat;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.alarmTitleText);
            dateTime = itemView.findViewById(R.id.alarmDateTimeText);
            repeat = itemView.findViewById(R.id.alarmRepeatText);
        }
    }
}
