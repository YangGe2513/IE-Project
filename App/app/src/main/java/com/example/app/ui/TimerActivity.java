package com.example.app.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityTimerBinding;

public class TimerActivity extends AppCompatActivity {

    private ActivityTimerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // When user click on this editText, it will show a timePicker to set the time.
        binding.editTextTime.setOnClickListener(view -> {
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    binding.editTextTime.setText(hour+":"+minute);
                }
            },0,0,true).show();
        });

        // Click this button and start countdown.
        binding.button.setOnClickListener(view -> {
            String timeText = binding.editTextTime.getText().toString();
            int hour = Integer.parseInt(timeText.split(":")[0]);
            int minute = Integer.parseInt(timeText.split(":")[1]);
            long time = (hour * 3600 + minute * 60) * 1000;
            new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    int seconds = (int) millisUntilFinished / 1000;
                    int hour = seconds / 3600;
                    int minute = (seconds % 3600) / 60;
                    int second = seconds % 60;
                    binding.textView.setText(hour+":"+minute+":"+second);
                }

                public void onFinish() {
                    binding.textView.setText("done!");
                }
            }.start();
        });
    }
}