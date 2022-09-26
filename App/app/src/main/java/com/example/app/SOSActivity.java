package com.example.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.data.ContactViewModel;
import com.example.app.data.model.Contact;
import com.example.app.databinding.ActivityContactDetailBinding;
import com.example.app.databinding.ActivitySosBinding;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


public class SOSActivity extends AppCompatActivity {

    private ActivitySosBinding binding;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;
    private long timeLeftInMillis = START_TIME_IN_MILLIS_2;
    private static final long START_TIME_IN_MILLIS_2 = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        Button sosHomeButton = binding.sosHomeButton;

        sosHomeButton.setOnClickListener(view -> {
            openMainActivity();
        });

        Button sosStartBtn = binding.sosStartBtn;
        sosStartBtn.setOnClickListener(view -> {
            showCountDownDialog();
        });

    }

    private void showCountDownDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sos_count_down_layout);

        TextView textViewCancel = dialog.findViewById(R.id.textViewSosCancel);
        Button yesButton = dialog.findViewById(R.id.btn_sos_countdown);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int s = (int) ((timeLeftInMillis / 1000) % 60);
                yesButton.setText(String.valueOf(s));
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = START_TIME_IN_MILLIS_2;
                showSuccessDialog();
                playSound(R.raw.sos1);
                dialog.dismiss();

            }
        }.start();


        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLeftInMillis = START_TIME_IN_MILLIS_2;
                countDownTimer.cancel();
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private void showSuccessDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sos_success_layout);

        Button callPoliceButton = dialog.findViewById(R.id.sos_call_police_btn);
        Button backToHomeButton = dialog.findViewById(R.id.sos_success_back_home_btn);
        Button sosFakeVoiceButton = dialog.findViewById(R.id.sos_fake_voice_btn);
        callPoliceButton.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(this,"Calling the police......", Toast.LENGTH_SHORT).show() ;
        });

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openMainActivity();
            }
        });
        sosFakeVoiceButton.setOnClickListener(view -> {
            Toast.makeText(this,"Fake voice playing......", Toast.LENGTH_SHORT).show() ;
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
}