package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySelectTypeBinding;

public class SelectTypeActivity extends AppCompatActivity {

    private ActivitySelectTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this,FollowMeActivity.class);
        binding.walking.setOnClickListener(view ->{
            intent.putExtra("type","Walking");
        });
        binding.running.setOnClickListener(view ->{
            intent.putExtra("type","Running");
        });
        binding.cycling.setOnClickListener(view ->{
            intent.putExtra("type","Cycling");
        });
        binding.dating.setOnClickListener(view ->{
            intent.putExtra("type","Dating");
        });
        binding.boating.setOnClickListener(view ->{
            intent.putExtra("type","Boating");
        });
        binding.driving.setOnClickListener(view ->{
            intent.putExtra("type","Driving");
        });

        binding.saveTypeButton.setOnClickListener(view -> {
            startActivity(intent);
            finish();
        });
    }
}