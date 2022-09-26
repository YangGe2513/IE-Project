package com.example.app.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startAppButton.setOnClickListener(view -> {
            if (binding.nameEditText.getText().toString().isEmpty()){
                binding.textInputLayout.setError("Name cannot be empty!");
                return;
            }
            finishOnBoarding();
        });
    }

    private void finishOnBoarding(){
        String username = binding.nameEditText.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsFinished",true);
        editor.putString("Username",username);
        editor.apply();
        Intent intent = new Intent(this, SuccessActivity.class);
        startActivity(intent);
        finish();
    }
}