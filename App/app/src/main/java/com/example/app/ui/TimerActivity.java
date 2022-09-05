package com.example.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;

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

        String countdownTimeString = getIntent().getStringExtra("time");

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        long time = 0;
        try {
            time = format.parse(countdownTimeString).getTime();
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
    }
}