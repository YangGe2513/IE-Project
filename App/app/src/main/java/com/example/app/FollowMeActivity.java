package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.databinding.ActivityFollowMeBinding;

public class FollowMeActivity extends AppCompatActivity {

    private ActivityFollowMeBinding binding;

    private ActivityResultLauncher<Intent> selectTypeActivityLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                String message=data.getStringExtra("type");
                                binding.typeText.setText(message);
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent start = new Intent(this,MapsActivity.class);

        binding.type.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
            selectTypeActivityLauncher.launch(intent);
        });

        binding.time.setOnClickListener(view ->{
            new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    String countdownTime = hour+"h "+minute+"min";
                    binding.timeText.setText(countdownTime);
                    start.putExtra("hour",hour);
                    start.putExtra("minute",minute);
                }
            },0,0,true).show();
        });

        binding.from.setOnClickListener(view ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.edit_location_dialog, null);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.show();
            final EditText editTextAddress = dialogView.findViewById(R.id.nameEditText);
            Button saveButton = dialogView.findViewById(R.id.btnBackToHome);
            saveButton.setOnClickListener(v -> {
                String address = editTextAddress.getText().toString();
                binding.fromText.setText(address);
                alertDialog.dismiss();
            });
            Button cancelButton = dialogView.findViewById(R.id.cancelEditLocationButton);
            cancelButton.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
        });

        binding.to.setOnClickListener(view ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.edit_location_dialog, null);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.show();
            final EditText editTextAddress = dialogView.findViewById(R.id.nameEditText);
            Button saveButton = dialogView.findViewById(R.id.btnBackToHome);
            saveButton.setOnClickListener(v -> {
                String address = editTextAddress.getText().toString();
                binding.toText.setText(address);
                alertDialog.dismiss();
            });
            Button cancelButton = dialogView.findViewById(R.id.cancelEditLocationButton);
            cancelButton.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
        });

        binding.contact.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.note.setOnClickListener(view ->{
            Intent intent = new Intent(this,SelectTypeActivity.class);
        });

        binding.startActivityButton.setOnClickListener(view ->{
            startActivity(start);
            finish();
        });
    }
}