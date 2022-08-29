package com.example.app.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.app.MapsActivity;
import com.example.app.databinding.FragmentHomeBinding;
import com.example.app.ui.TimerActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Map
        binding.mapView.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });

        binding.timerButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TimerActivity.class);
            startActivity(intent);
        });

        // Emergency call 1
        binding.callButton1.setOnClickListener(view -> {
            String contact1 = sharedPreferences.getString("contact1", "");
            call(contact1);
        });

        // Emergency call 2
        binding.callButton2.setOnClickListener(view -> {
            String contact2 = sharedPreferences.getString("contact2", "");
            call(contact2);
        });

        // Call police
        binding.policeButton.setOnClickListener(view -> {
            String dialMode = sharedPreferences.getString("dial_mode","");
            if ("direct_dial".equals(dialMode)){
                call("000");
                return;
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:000");
            intent.setData(data);
            startActivity(intent);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}