package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityFollowMeBinding;

public class FollowMeActivity extends AppCompatActivity {

    private ActivityFollowMeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startActivityButton.setOnClickListener(view ->{
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        });
    }
}