package com.example.app;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.app.data.LocationResponse;
import com.example.app.data.RetrofitClient;
import com.example.app.databinding.ActivityMapsBinding;
import com.example.app.interfaces.RetrofitInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private long START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS_2 = 4000;
    private TextView mTextViewCountDown;

    private Button mButtonStartPause;
    private Button mButtonReset;


    private CountDownTimer mCountDownTimer;
    private CountDownTimer countDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;
    private long timeLeftInMillis = START_TIME_IN_MILLIS_2;

    private static final int REQUEST_LOCATION = 1;
    private RetrofitInterface retrofitInterface;
    private LocationManager locationManager;
    private String lat = "-37.913903";
    private String lon = "145.131741";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int hour = getIntent().getIntExtra("hour",0);
        int minute = getIntent().getIntExtra("minute",0);

        START_TIME_IN_MILLIS = hour * 3600000L + minute * 60000L;

        mTimeLeftInMillis = START_TIME_IN_MILLIS;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_countdown_panic);
        mButtonReset = findViewById(R.id.button_countdown_finish);

        startTimer();

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                    showAlertDialog();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                showDialog();
            }
        });

        updateCountDownText();





        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        binding.btnSetLocation.setOnClickListener(view -> {
            refreshLocation();
                }
        );
        binding.btnBackToHome.setOnClickListener(view -> {
            finish();
        });

        refreshLocation();
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lati = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                lat = String.valueOf(lati);
                lon = String.valueOf(longi);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setLocation(){
        retrofitInterface = RetrofitClient.getGoogleLocation();
        Call<LocationResponse> locationResponseCall = retrofitInterface.getGoogleLocation(lat+","+lon,getString(R.string.google_api_key));
        locationResponseCall.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call,
                                   Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        LocationResponse jsonObj = response.body();
                        ArrayList<LocationResponse.Result> main = jsonObj.results;
                        LocationResponse.Result address = main.get(0);
                        LocationResponse.AddressComponent city = address.address_components.get(1);
                        final TextView textView = binding.textViewLocate;
                        String locationText = "I am in "+ city.long_name.toString()+". " ;
                        textView.setText(locationText);
                        getIntent().putExtra("location",locationText);

                    } catch (Exception e) {
                        Log.i("Error ", "Assign failed");
                    }

                } else {
                    Log.i("Error ", "Response failed");
                }
            }
            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t){
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void refreshLocation(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
            setLocation();
        }
    }
    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        Button doneButton = dialog.findViewById(R.id.done_button);
        Button homeButton = dialog.findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showAlertDialog() {

        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.custom_dialog_layout);

        Button cancelButton = alertDialog.findViewById(R.id.btn_cancel);
        Button yesButton = alertDialog.findViewById(R.id.btn_yes);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showCountDownDialog();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogPopAnimation;
        alertDialog.getWindow().setGravity(Gravity.CENTER);

    }


    private void showCountDownDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_count_down_layout);

        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        Button yesButton = dialog.findViewById(R.id.btn_countdown);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int s = (int) ((timeLeftInMillis / 1000) % 60);
                yesButton.setText(String.valueOf(s));
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = START_TIME_IN_MILLIS_2;

                String phoneNumber = getIntent().getStringExtra("phoneNumber");
                String googleMapLink = "https://www.google.com/maps?q="+lat+","+lon;

                String msgText = getIntent().getStringExtra("location")
                        + " Link: " + googleMapLink
                        + " I'm from "
                        + getIntent().getStringExtra("from")
                        + " to " + getIntent().getStringExtra("to") + "."
                        + " Please help me!";
                sentMsg(phoneNumber,msgText);
                showSuccessDialog();
                dialog.dismiss();

            }
        }.start();


        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLeftInMillis = START_TIME_IN_MILLIS_2;
                countDownTimer.cancel();
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showSuccessDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_call_layout);

        Button callPoliceButton = dialog.findViewById(R.id.call_police_btn);
        Button backToHomeButton = dialog.findViewById(R.id.back_home_btn);

        callPoliceButton.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(this,"Calling the police......", Toast.LENGTH_SHORT).show() ;
        });

        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }








    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Continue");
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("Panic");
    }


    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Continue");
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mTextViewCountDown.setText("00:00:00");
        mTimerRunning = false;
        mCountDownTimer.cancel();
        mButtonStartPause.setText("Continue");
    }

    private void updateCountDownText() {
        int h = (int) ((mTimeLeftInMillis / 1000) / 3600);
        int m = (int) (((mTimeLeftInMillis / 1000) / 60) % 60);
        int s = (int) ((mTimeLeftInMillis / 1000) % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", h,m, s);

        mTextViewCountDown.setText(timeLeftFormatted);
    }












    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        mMap.addMarker(new MarkerOptions().position(location).title("I'm here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(25.0f);

    }


    private void sentMsg(String mobileNum, String msgText){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobileNum,null,msgText,null,null);
            Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}