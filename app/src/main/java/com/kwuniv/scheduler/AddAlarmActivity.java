package com.kwuniv.scheduler;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAlarmActivity extends AppCompatActivity {

    private EditText titleEditText, dateEditText, timeEditText;
    private RadioGroup repeatRadioGroup;
    private TextView saveButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        // View 초기화
        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        repeatRadioGroup = findViewById(R.id.repeatRadioGroup);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> saveAlarm());

        // 뒤로가기 버튼 클릭 리스너
        backButton.setOnClickListener(v -> finish());
    }

    private void saveAlarm() {
        String title = titleEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
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
