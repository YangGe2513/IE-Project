package com.example.app.ui;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityTimerBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                    String hourString = hour + "";
                    String minuteString = minute + "";
                    if(hour < 10){
                        hourString = "0"+hour;
                    }
                    if(minute < 10){
                        minuteString = "0"+minute;
                    }
                    binding.editTextTime.setText(hourString+":"+minuteString);
                }
            },0,0,true).show();
        });

        // Click this button and start countdown.
        binding.button.setOnClickListener(view -> {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String timeText = binding.editTextTime.getText().toString();
            long time = 0;
            try {
                time = format.parse(timeText).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set message
            builder.setMessage("Time is up!")
                    .setTitle("Notification");
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();

            new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(millisUntilFinished);
                    Date date = calendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String countDownText = format.format(date);
                    binding.textView.setText(countDownText);
                }

                public void onFinish() {
                    // Do something
                    dialog.show();
                }
            }.start();
        });
    }
}