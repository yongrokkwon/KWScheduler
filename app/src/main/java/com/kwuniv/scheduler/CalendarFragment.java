package com.kwuniv.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private GridView calendarGridView;
    private CalendarAdapter calendarAdapter;
    private HashMap<String, List<String>> scheduleMap;
    private Calendar calendar;
    private TextView currentMonthText;

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarGridView = view.findViewById(R.id.calendarGridView);
        currentMonthText = view.findViewById(R.id.currentMonthText);
        ImageButton prevMonthButton = view.findViewById(R.id.prevMonthButton);
        ImageButton nextMonthButton = view.findViewById(R.id.nextMonthButton);

        // Initialize schedule data
        calendar = Calendar.getInstance();
        scheduleMap = new HashMap<>();
//        populateDummyData();

        // Generate dates for the current month
        List<String> dates = generateDates();

        // Set adapter
        updateCalendar(); // Set initial dates and month
        calendarAdapter = new CalendarAdapter(getContext(), dates, scheduleMap);
        calendarGridView.setAdapter(calendarAdapter);

        // Set item click listener
        calendarGridView.setOnItemClickListener((parent, view1, position, id) -> {
            String clickedDate = dates.get(position);
            if (!clickedDate.isEmpty()) {
                // Pass data to "일정목록" fragment (optional)
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", clickedDate);

                Fragment notificationsFragment = new NotificationsFragment();
                notificationsFragment.setArguments(bundle);

                // Navigate to "일정목록" tab
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToTab(R.id.nav_notifications);
                }
            }
        });

        // Set button listeners
        prevMonthButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            loadAlarmsForCurrentMonth();
        });

        nextMonthButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            loadAlarmsForCurrentMonth();
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAlarmsForCurrentMonth();
    }

    private void loadAlarmsForCurrentMonth() {
        // Update the current month text
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월", Locale.getDefault());
        currentMonthText.setText(sdf.format(calendar.getTime()));

        // Update GridView with dates of the current month
        List<String> dates = generateDatesForMonth();
        calendarAdapter.updateDates(dates);

        // Fetch alarms for the current month
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based

        List<Alarm> alarms = SharedPreferenceManager.getInstance(getContext()).getAlarmsByMonth(year, month);

        // Update RecyclerView with alarms
        if (alarmAdapter == null) {
            alarmAdapter = new AlarmAdapter(alarms);
            recyclerView.setAdapter(alarmAdapter);
        } else {
            alarmAdapter.updateAlarms(alarms);
        }
    }



    private List<String> generateDatesForMonth() {
        List<String> dates = new ArrayList<>();

        // Set the calendar to the first day of the month
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty dates for alignment (days before the 1st)
        for (int i = 0; i < firstDayOfWeek; i++) {
            dates.add(""); // Empty placeholder
        }

        // Add actual days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            dates.add(String.valueOf(day));
        }

        return dates;
    }

    // Update Calendar Fragment Java code
    private void updateCalendar() {
        // Get the current month and year in Korean format
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        int year = calendar.get(Calendar.YEAR);
        currentMonthText.setText(String.format(Locale.KOREAN, "%d년 %d월", year, month));

        // Update dates in the adapter
        List<String> dates = generateDates();
        calendarAdapter = new CalendarAdapter(getContext(), dates, scheduleMap);
        calendarGridView.setAdapter(calendarAdapter);
    }

    private List<String> generateDates() {
        List<String> dates = new ArrayList<>();

        // Set calendar to the first day of the month
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the day of the week for the first day (for alignment)
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty strings for alignment
        for (int i = 0; i < firstDayOfWeek; i++) {
            dates.add("");
        }

        // Add actual days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            dates.add(String.valueOf(day));
        }

        return dates;
    }

    private void populateDummyData() {
        // Add some sample schedule data
        scheduleMap.put("1", Arrays.asList("강의평가", "장학금 신청"));
        scheduleMap.put("8", Arrays.asList("기말고사", "보강", "추가 일정"));
        scheduleMap.put("15", Arrays.asList("기말고사 종료", "2학기 종료"));
    }
}
