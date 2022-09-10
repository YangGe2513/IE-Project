package com.example.app;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySmartCallBinding;
import com.example.app.ui.TimerActivity;

public class SmartCallActivity extends AppCompatActivity {

    private ActivitySmartCallBinding binding;
    private String countdownTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySmartCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // When user click on this editText, it will show a timePicker to set the time.
        binding.editTextTime.setOnClickListener(view -> {
            new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    String countdownTime = hour+"h "+minute+"min";
                    binding.editTextTime.setText(countdownTime);

                    String hourString = hour + "";
                    String minuteString = minute + "";
                    if(hour < 10){
                        hourString = "0"+hour;
                    }
                    if(minute < 10){
                        minuteString = "0"+minute;
                    }
                    countdownTimeString = hourString + ":" + minuteString;
                }
            },0,0,true).show();
        });

        // Pass countdown timeString to TimerActivity.
        binding.startButton.setOnClickListener(view -> {
            if(countdownTimeString == null){
                return;
            }
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra("time",countdownTimeString);
            startActivity(intent);
        });
    }
}