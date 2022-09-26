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

        Intent result = getIntent();
        String type = result.getStringExtra("type");
        binding.typeText.setText(type);

        binding.type.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
            startActivity(intent);
        });

        binding.time.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.from.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.to.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.contact.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.note.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.startActivityButton.setOnClickListener(view ->{
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        });
    }
}