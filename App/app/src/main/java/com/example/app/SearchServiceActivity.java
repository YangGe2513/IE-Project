package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySearchServiceBinding;

public class SearchServiceActivity extends AppCompatActivity {

    private ActivitySearchServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton2.setOnClickListener(view -> {
            finish();
        });
    }
}