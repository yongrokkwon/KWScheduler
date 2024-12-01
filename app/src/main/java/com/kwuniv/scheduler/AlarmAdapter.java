package com.kwuniv.scheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private final List<Alarm> alarmList;

    public AlarmAdapter(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);

        // Set data
        holder.title.setText(alarm.getTitle());
        holder.dateTime.setText(String.format("%s %s", alarm.getDate(), alarm.getTime()));
        holder.repeat.setText(String.format("반복: %s", alarm.getRepeat()));
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void updateAlarms(List<Alarm> newAlarms) {
        alarmList.clear();
        alarmList.addAll(newAlarms);
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
