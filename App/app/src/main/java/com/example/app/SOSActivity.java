package com.example.app;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.app.data.LocationResponse;
import com.example.app.data.RetrofitClient;
import com.example.app.databinding.ActivitySosBinding;
import com.example.app.interfaces.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SOSActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private RetrofitInterface retrofitInterface;
    private LocationManager locationManager;
    private ActivitySosBinding binding;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;
    private long timeLeftInMillis = START_TIME_IN_MILLIS_2;
    private static final long START_TIME_IN_MILLIS_2 = 4000;

    private String lat = "-37.913903";
    private String lon = "145.131741";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button sosHomeButton = binding.sosHomeButton;

        sosHomeButton.setOnClickListener(view -> {
            finish();
        });

        Button sosStartBtn = binding.sosStartBtn;
        sosStartBtn.setOnClickListener(view -> {
            showCountDownDialog();
        });
        refreshLocation();
    }

    private void showCountDownDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sos_count_down_layout);

        TextView textViewCancel = dialog.findViewById(R.id.textViewSosCancel);
        Button yesButton = dialog.findViewById(R.id.btn_sos_countdown);

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

                /**
                 *
                 * To do
                 * Implement contact select
                 *
                 */


                String phoneNumber = "77889";
                String googleMapLink = "https://www.google.com/maps?q="+lat+","+lon;

                String msgText = getIntent().getStringExtra("location")
                        + " Link: " + googleMapLink
                        + " I can't talk right now. Please help me!";
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
        dialog.setContentView(R.layout.sos_success_layout);

        Button callPoliceButton = dialog.findViewById(R.id.sos_call_police_btn);
        Button backToHomeButton = dialog.findViewById(R.id.sos_success_back_home_btn);
        Button sosFakeVoiceButton = dialog.findViewById(R.id.sos_fake_voice_btn);

        SharedPreferences onBoardingSharedPreferences =
                this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        callPoliceButton.setOnClickListener(view -> {


                String dialMode = sharedPreferences.getString("dial_mode","");
                if ("direct_dial".equals(dialMode)){
                    call("000");
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:000");
                intent.setData(data);
                startActivity(intent);

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
        sosFakeVoiceButton.setOnClickListener(view -> {
            playSound(R.raw.sos1);
            Toast.makeText(this,"Siren playing......", Toast.LENGTH_SHORT).show() ;
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void playSound(int resourceId){
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, resourceId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mp = null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
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
                        String locationText = "I'm' in "+ city.long_name.toString()+". " ;

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

    private void sentMsg(String mobileNum, String msgText){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobileNum,null,msgText,null,null);

        }
        catch (Exception e){
        }
    }

    private void call(String contactNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + contactNumber);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
        }
        intent.setData(data);
        startActivity(intent);
    }
}