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

        Intent intent = getIntent();
        binding.walking.setOnClickListener(view ->{
            resetImage();
            binding.walking.setImageResource(R.drawable.ic_walking_selected);
            intent.putExtra("type","Walking");
        });
        binding.running.setOnClickListener(view ->{
            resetImage();
            binding.running.setImageResource(R.drawable.ic_running_selected);
            intent.putExtra("type","Running");
        });
        binding.cycling.setOnClickListener(view ->{
            resetImage();
            binding.cycling.setImageResource(R.drawable.ic_cycling_selected);
            intent.putExtra("type","Cycling");
        });
        binding.dating.setOnClickListener(view ->{
            resetImage();
            binding.dating.setImageResource(R.drawable.ic_dating_selected);
            intent.putExtra("type","Dating");
        });
        binding.boating.setOnClickListener(view ->{
            resetImage();
            binding.boating.setImageResource(R.drawable.ic_boating_selected);
            intent.putExtra("type","Boating");
        });
        binding.driving.setOnClickListener(view ->{
            resetImage();
            binding.driving.setImageResource(R.drawable.ic_driving_selected);
            intent.putExtra("type","Driving");
        });

        binding.saveTypeButton.setOnClickListener(view -> {
            setResult(RESULT_OK,intent);
            finish();
        });
    }

    private void resetImage(){
        binding.walking.setImageResource(R.drawable.ic_walking);
        binding.running.setImageResource(R.drawable.ic_running);
        binding.cycling.setImageResource(R.drawable.ic_cycling);
        binding.dating.setImageResource(R.drawable.ic_dating);
        binding.boating.setImageResource(R.drawable.ic_boating);
        binding.driving.setImageResource(R.drawable.ic_driving);
    }
}