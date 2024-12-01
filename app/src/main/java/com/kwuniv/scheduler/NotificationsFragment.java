package com.kwuniv.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private HashMap<String, List<String>> scheduleMap;
    AlarmAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView scheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set the adapter
        List<Alarm> alarms = SharedPreferenceManager.getInstance(getContext()).getAlarms();
        adapter = new AlarmAdapter(alarms);
        scheduleRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Alarm> alarms = SharedPreferenceManager.getInstance(getContext()).getAlarms();
        adapter.updateAlarms(alarms);
    }
}
