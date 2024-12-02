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
    RecyclerView scheduleRecyclerView;
    View emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        scheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);
        emptyView = view.findViewById(R.id.emptyView);
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
        updateUI();
    }

    private void updateUI() {
        List<Alarm> alarms = SharedPreferenceManager.getInstance(getContext()).getAlarms();
        if (alarms.isEmpty()) {
            scheduleRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            scheduleRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        adapter.updateAlarms(alarms);
    }
}
