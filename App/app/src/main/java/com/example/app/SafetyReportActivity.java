package com.example.app;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivitySafetyReportBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SafetyReportActivity extends AppCompatActivity {

    private ActivitySafetyReportBinding binding;
    private HashMap<String, String> siteData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySafetyReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        readData();

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
            String crimeIndex = siteData.get(postcode);
            binding.crimeRate.setText(crimeIndex+"%");
            String safetyIndex = 100 - Integer.parseInt(crimeIndex)+ "%";
            binding.safetyRate.setText(safetyIndex);
            return;
        }
        binding.crimeRate.setText("??");
        binding.safetyRate.setText("??");
        dataNotFound();

//        int safetyRate = randomPercentage(60,100);
//        binding.safetyRate.setText(safetyRate+"%");
//        double coefficient = randomPercentage(8,12)*0.1;
//        int crimeRate = (int) ((100-safetyRate)*coefficient);
//        binding.crimeRate.setText(crimeRate+"%");
    }

    private void readData() {
        InputStream is = getResources().openRawResource(R.raw.crime_index_vic);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                siteData.put(tokens[0],tokens[1]);
            }
        } catch (IOException ignored) {

        }
    }

    private void dataNotFound(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cannot find data in this area.")
                .setTitle("Sorry");
        builder.setPositiveButton("OK", (dialog, id) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}