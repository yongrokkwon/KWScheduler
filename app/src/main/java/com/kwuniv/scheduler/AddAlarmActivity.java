package com.kwuniv.scheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * AddAlarmActivity
 * 사용자가 알림을 추가하는 화면을 제공하는 Activity
 * - 날짜, 시간, 제목, 반복 여부를 설정할 수 있음
 * - 설정한 알림은 SharedPreferences에 저장되고, AlarmManager를 통해 알림이 등록
 */
public class AddAlarmActivity extends AppCompatActivity {

    private EditText titleEditText;
    private RadioGroup repeatRadioGroup;
    private TextView saveButton, dateTextView, timeTextView;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        // View 초기화
        titleEditText = findViewById(R.id.titleEditText);
        repeatRadioGroup = findViewById(R.id.repeatRadioGroup);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> saveAlarm());

        // 뒤로가기 버튼 클릭 리스너
        backButton.setOnClickListener(v -> finish());


        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        // Date picker dialog
        dateTextView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                dateTextView.setText(selectedDate);
                dateTextView.setTextColor(getResources().getColor(android.R.color.black));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker dialog
        timeTextView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                timeTextView.setText(selectedTime);
                timeTextView.setTextColor(getResources().getColor(android.R.color.black));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });
    }

    /**
     * saveAlarm
     * 사용자가 입력한 데이터를 기반으로 알림을 저장하고 등록
     * - 제목, 날짜, 시간, 반복 여부를 확인
     * - 유효성 검사를 통과한 경우, SharedPreferences에 알림을 저장하고 AlarmManager에 등록
     */
    private void saveAlarm() {
        String title = titleEditText.getText().toString();
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();
        int selectedRepeatId = repeatRadioGroup.getCheckedRadioButtonId();
        String repeat = "없음";

        if (selectedRepeatId != -1) {
            RadioButton selectedRepeatButton = findViewById(selectedRepeatId);
            repeat = selectedRepeatButton.getText().toString();
        }

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Alarm alarm = new Alarm(title, date, time, repeat);
        SharedPreferenceManager.getInstance(this).saveAlarm(alarm);

        registerAlarm(alarm);

        Toast.makeText(this, "알림이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * registerAlarm
     * AlarmManager를 사용하여 알림을 등록
     * - 알림 시간(Calendar)을 설정하고, 현재 시각보다 이후일 경우에만 등록
     *
     * @param alarm 사용자가 설정한 알람 객체
     */
    private void registerAlarm(Alarm alarm) {
        AlarmHelper alarmHelper = new AlarmHelper(this);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(alarm.getDate() + " " + alarm.getTime()));

            // Register the alarm if the time is in the future
            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                alarmHelper.setAlarm(alarm.hashCode(), calendar, alarm.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "알람을 등록할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
