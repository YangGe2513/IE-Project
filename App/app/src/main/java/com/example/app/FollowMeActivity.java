package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.adapter.ContactButtonRecyclerViewAdapter;
import com.example.app.data.ContactViewModel;
import com.example.app.data.model.Event;
import com.example.app.databinding.ActivityFollowMeBinding;

public class FollowMeActivity extends AppCompatActivity {

    private ActivityFollowMeBinding binding;
    private Event event;


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

        event = new Event();

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
            showEditLocationDialog(view,binding.fromText,"from",start);
        });

        binding.to.setOnClickListener(view ->{
            showEditLocationDialog(view,binding.toText,"to", start);
        });

        binding.contact.setOnClickListener(view ->{
            showPickContactDialog(view,binding.contactText);
        });

        binding.note.setOnClickListener(view ->{
            showEditNoteDialog(view,binding.noteText,start);
        });

        binding.startActivityButton.setOnClickListener(view ->{

            String number = getIntent().getStringExtra("phoneNumber");
            start.putExtra("phoneNumber",number);

            startActivity(start);
            finish();
        });
    }

    public void showEditLocationDialog(@NonNull View view, TextView textView,String fromTo, Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_edit_location, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.show();
        final EditText editTextAddress = dialogView.findViewById(R.id.locationEditText);
        Button saveButton = dialogView.findViewById(R.id.btnBackToHome);
        saveButton.setOnClickListener(v -> {
            String address = editTextAddress.getText().toString();
            textView.setText(address);
            intent.putExtra(fromTo,address);
            alertDialog.dismiss();
        });
        Button cancelButton = dialogView.findViewById(R.id.cancelEditNoteButton);
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }

    public void showPickContactDialog(@NonNull View view, TextView textView){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_pick_contact, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.show();

        ContactViewModel contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        RecyclerView recyclerView = dialogView.findViewById(R.id.contactRecyclerView);
        contactViewModel.getAll().observe(this,contactList -> {
            ContactButtonRecyclerViewAdapter adapter = new ContactButtonRecyclerViewAdapter(this,contactList,alertDialog,textView);
            recyclerView.setAdapter(adapter);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getParent(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        Button cancelButton = dialogView.findViewById(R.id.cancelSelectContactButton);
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }

    public void showEditNoteDialog(@NonNull View view, TextView textView, Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_edit_note, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.show();
        final EditText noteEditText = dialogView.findViewById(R.id.noteEditText);
        noteEditText.setText(event.getNote());
        Button saveButton = dialogView.findViewById(R.id.btnBackToHome);
        saveButton.setOnClickListener(v -> {
            event.setNote(noteEditText.getText().toString());
            textView.setText("edited");
            intent.putExtra("note",noteEditText.getText().toString());
            alertDialog.dismiss();

        });
        Button cancelButton = dialogView.findViewById(R.id.cancelEditNoteButton);
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }




}