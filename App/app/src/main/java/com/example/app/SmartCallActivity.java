package com.example.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySmartCallBinding;
import com.example.app.ui.TimerActivity;

public class SmartCallActivity extends AppCompatActivity {

    private ActivitySmartCallBinding binding;
    private String countdownTimeString;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySmartCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dogImageView.setOnClickListener(view->{
            playSound(R.raw.dog);
        });

        binding.dadImageView.setOnClickListener(view->{
            playSound(R.raw.man1);
        });

        binding.manImageView.setOnClickListener(view->{
            playSound(R.raw.man2);
        });

        binding.fireworkImageView.setOnClickListener(view->{
            playSound(R.raw.lightning);
        });

//        // When user click on this editText, it will show a timePicker to set the time.
//        binding.editTextTime.setOnClickListener(view -> {
//            new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                    String countdownTime = hour+"h "+minute+"min";
//                    binding.editTextTime.setText(countdownTime);
//
//                    String hourString = hour + "";
//                    String minuteString = minute + "";
//                    if(hour < 10){
//                        hourString = "0"+hour;
//                    }
//                    if(minute < 10){
//                        minuteString = "0"+minute;
//                    }
//                    countdownTimeString = hourString + ":" + minuteString;
//                }
//            },0,0,true).show();
//        });

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

    private void playSound(int resourceId){
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, resourceId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mp = null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}