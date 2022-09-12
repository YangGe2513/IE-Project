package com.example.app.ui.notifications;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.R;
import com.example.app.data.LocationResponse;
import com.example.app.data.RetrofitClient;
import com.example.app.databinding.FragmentNotificationsBinding;
import com.example.app.interfaces.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private static final int REQUEST_LOCATION = 1;
    private FragmentNotificationsBinding binding;
    private RetrofitInterface retrofitInterface;
    private LocationManager locationManager;
    private String lat = "-37.913903";
    private String lon = "145.131741";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ActivityCompat.requestPermissions(requireActivity(),
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        //get location button
        binding.button.setOnClickListener(view -> {

            locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
                setLocation();
            }
        }
        );

        return root;
    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity().getApplicationContext());
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
                requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lati = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                lat = String.valueOf(lati);
                lon = String.valueOf(longi);
            } else {
                Toast.makeText(requireActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(requireContext(),"I am in "+ city.long_name.toString() + ". Latitude:" +lat+ ", Longitude:"+lon+".", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.i("Error ", "Assign failed");
                    }

                } else {
                    Log.i("Error ", "Response failed");
                }
            }
            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t){
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}