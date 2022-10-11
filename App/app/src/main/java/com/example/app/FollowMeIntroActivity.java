package com.example.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.app.databinding.ActivityFollowMeIntroBinding;

public class FollowMeIntroActivity extends AppCompatActivity {

    private ActivityFollowMeIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowMeIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        binding.backButton.setOnClickListener(view -> {
            finish();
        });

        binding.nextButton.setOnClickListener(view -> {
            startActivity(new Intent(this, FollowMeActivity.class));
            finish();
        });
    }
}