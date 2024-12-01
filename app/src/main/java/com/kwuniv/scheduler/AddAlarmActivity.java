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

import java.util.Calendar;

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

        Toast.makeText(this, "알림이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
