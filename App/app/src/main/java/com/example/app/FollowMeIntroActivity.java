package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityFollowMeIntroBinding;

public class FollowMeIntroActivity extends AppCompatActivity {

    private ActivityFollowMeIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowMeIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> {
            finish();
        });

        binding.nextButton.setOnClickListener(view -> {
            startActivity(new Intent(this, FollowMeActivity.class));
            finish();
        });
    }
}