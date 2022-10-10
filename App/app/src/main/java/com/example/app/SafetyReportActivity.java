package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySafetyReportBinding;

public class SafetyReportActivity extends AppCompatActivity {

    private ActivitySafetyReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySafetyReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String postcode = getIntent().getStringExtra("postcode");
        if(!"3168".equals(postcode)){
            int safetyRate = randomPercentage(60,100);
            binding.safetyRate.setText(safetyRate+"%");
            double coefficient = randomPercentage(8,12)*0.1;
            int crimeRate = (int) ((100-safetyRate)*coefficient);
            binding.crimeRate.setText(crimeRate+"%");
        }
        binding.backHomeButton.setOnClickListener(view ->{
            finish();
        });
    }

    public int randomPercentage(int min, int max){
        int range = (int) ((max-min) * Math.random());
        return min+range;
    }
}