package com.example.app.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.ContactDetailActivity;
import com.example.app.FollowMeActivity;
import com.example.app.R;
import com.example.app.adapter.ContactButtonRecyclerViewAdapter;
import com.example.app.data.ContactViewModel;
import com.example.app.data.LocationResponse;
import com.example.app.data.RetrofitClient;
import com.example.app.databinding.FragmentHomeBinding;
import com.example.app.interfaces.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ContactViewModel contactViewModel;
    private RecyclerView.LayoutManager layoutManager;
    private MediaPlayer mediaPlayer;
    private RetrofitInterface retrofitInterface;
    private LocationManager locationManager;
    private String lat = "-37.913903";
    private String lon = "145.131741";
    private static final int REQUEST_LOCATION = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Load data
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        SharedPreferences onBoardingSharedPreferences =
                requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = onBoardingSharedPreferences.getString("Username","");

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.welcomeTextView.setText("Hi, " + username);

        contactViewModel.getAll().observe(getViewLifecycleOwner(),contactList -> {
            ContactButtonRecyclerViewAdapter adapter = new ContactButtonRecyclerViewAdapter(getActivity(),contactList);
            binding.contactRecyclerView.setAdapter(adapter);
        });

        layoutManager = new LinearLayoutManager(getActivity().getParent(),LinearLayoutManager.HORIZONTAL,false);
        binding.contactRecyclerView.setLayoutManager(layoutManager);


        ActivityCompat.requestPermissions(requireActivity(),
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        // FollowMe
        binding.followMe.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FollowMeActivity.class);
            startActivity(intent);
        });


        // Add emergency contact
        binding.addContactButton.setOnClickListener(view -> {
            contactViewModel.getAll().observe(getViewLifecycleOwner(),contactList -> {
                int maximum = getResources().getInteger(R.integer.contacts_max);
                if(contactList.size() < maximum){
                    Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
                    intent.putExtra("mode", "add");
                    startActivity(intent);
                }
                else {
                    maxContactDialog();
                }
            });
        });

        // Call police
//        binding.policeButton.setOnClickListener(view -> {
//            String dialMode = sharedPreferences.getString("dial_mode","");
//            if ("direct_dial".equals(dialMode)){
//                call("000");
//                return;
//            }
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            Uri data = Uri.parse("tel:000");
//            intent.setData(data);
//            startActivity(intent);
//        });

        binding.sos.setOnClickListener(view->{
            playSound(R.raw.sos1);
        });
        return root;
    }

    private void call(String contactNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + contactNumber);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},1);
        }
        intent.setData(data);
        startActivity(intent);
    }

    private void maxContactDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set message
        builder.setMessage("Beyond the maximum set for emergency contacts!")
                .setTitle("Error");
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void playSound(int resourceId){
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(getActivity(), resourceId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mp = null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }





    private void OnGPS() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity().getApplicationContext());
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
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
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







}