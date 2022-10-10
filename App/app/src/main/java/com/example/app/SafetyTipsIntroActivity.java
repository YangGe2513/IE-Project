package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySafetyTipsIntroBinding;

public class SafetyTipsIntroActivity extends AppCompatActivity {

    private ActivitySafetyTipsIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySafetyTipsIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> {
            finish();
        });

        binding.nextButton.setOnClickListener(view -> {
            startActivity(new Intent(this, SafetyTipsActivity.class));
            finish();
        });
    }
}