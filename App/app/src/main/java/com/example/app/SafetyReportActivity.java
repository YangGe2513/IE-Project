package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySafetyReportBinding;

import java.util.HashMap;

public class SafetyReportActivity extends AppCompatActivity {

    private ActivitySafetyReportBinding binding;
    private HashMap<String, String> siteData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySafetyReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        siteData.put("3168","79%,26%");
        siteData.put("3000","81%,17%");

        setData();

        binding.backHomeButton.setOnClickListener(view ->{
            finish();
        });
    }

    public int randomPercentage(int min, int max){
        int range = (int) ((max-min) * Math.random());
        return min+range;
    }

    private void setData(){
        String postcode = getIntent().getStringExtra("postcode");
        if(siteData.containsKey(postcode)){
            String[] data = siteData.get(postcode).split(",");
            binding.safetyRate.setText(data[0]);
            binding.crimeRate.setText(data[1]);
            return;
        }
        int safetyRate = randomPercentage(60,100);
        binding.safetyRate.setText(safetyRate+"%");
        double coefficient = randomPercentage(8,12)*0.1;
        int crimeRate = (int) ((100-safetyRate)*coefficient);
        binding.crimeRate.setText(crimeRate+"%");
    }
}