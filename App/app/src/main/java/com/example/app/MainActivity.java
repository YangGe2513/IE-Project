package com.example.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.app.databinding.ActivityMainBinding;
import com.example.app.onboarding.OnboardingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int MESSAGE_PERMISSION_CODE = 100;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        boolean isFinished = sharedPreferences.getBoolean("IsFinished",false);
        if(!isFinished){
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
            onDestroy();
        }
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        welcomeDialog();


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_contact,R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void welcomeDialog(){
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean welcome = defaultSharedPreferences.getBoolean("introduction",true);
        if(!welcome)
        {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_welcome, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.show();
        alertDialog.setCanceledOnTouchOutside(false);

        String[] titles = getResources().getStringArray(R.array.welcomeTitle);
        String[] descriptions = getResources().getStringArray(R.array.welcomeDescription);

        final TextView title = dialogView.findViewById(R.id.title);
        final TextView description = dialogView.findViewById(R.id.descriptionTextView);
        title.setText(titles[0]);
        description.setText(descriptions[0]);

        Button nextButton = dialogView.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            int i = 1;
            @Override
            public void onClick(View v) {
                if(i<titles.length){
                    title.setText(titles[i]);
                    description.setText(descriptions[i]);
                    i++;
                }
                else {
                    alertDialog.dismiss();
                }
            }
        });

        Button skipButton = dialogView.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean("introduction", false);
        editor.apply();
    }
}