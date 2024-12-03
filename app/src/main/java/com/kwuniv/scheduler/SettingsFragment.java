package com.kwuniv.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private Switch notificationSwitch;
    private Switch vibrationSwitch;
    private Switch soundSwitch; // 추가: 소리 설정

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Switch 초기화
        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        vibrationSwitch = view.findViewById(R.id.vibrationSwitch);
        soundSwitch = view.findViewById(R.id.soundSwitch);

        // 초기 상태 설정
        SharedPreferenceManager spManager = SharedPreferenceManager.getInstance(getContext());
        notificationSwitch.setChecked(spManager.isNotificationsEnabled());
        vibrationSwitch.setChecked(spManager.isVibrationEnabled());
        soundSwitch.setChecked(spManager.isSoundEnabled());

        // 알림 Switch 리스너
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spManager.setNotificationsEnabled(isChecked);
            updateDependentSwitches(isChecked);
        });

        // 진동 Switch 리스너
        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationSwitch.isChecked()) {
                spManager.setVibrationEnabled(isChecked);
            } else {
                vibrationSwitch.setChecked(false);
            }
        });

        // 소리 Switch 리스너
        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notificationSwitch.isChecked()) {
                spManager.setSoundEnabled(isChecked);
            } else {
                soundSwitch.setChecked(false);
            }
        });

        return view;
    }

    // 알림이 꺼지면 진동, 소리 비활성화
    private void updateDependentSwitches(boolean isNotificationEnabled) {
        vibrationSwitch.setEnabled(isNotificationEnabled);
        soundSwitch.setEnabled(isNotificationEnabled);

        if (!isNotificationEnabled) {
            vibrationSwitch.setChecked(false);
            soundSwitch.setChecked(false);
        }
    }
}
