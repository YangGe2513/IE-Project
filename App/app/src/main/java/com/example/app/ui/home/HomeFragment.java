package com.example.app.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.app.DataReportActivity;
import com.example.app.FollowMeIntroActivity;
import com.example.app.SOSActivity;
import com.example.app.SafetyTipsIntroActivity;
import com.example.app.adapter.ContactButtonRecyclerViewAdapter;
import com.example.app.data.ContactViewModel;
import com.example.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ContactViewModel contactViewModel;
    private RecyclerView.LayoutManager layoutManager;
    private MediaPlayer mediaPlayer;
    private static final int REQUEST_LOCATION = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Load data
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        SharedPreferences onBoardingSharedPreferences =
                requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireActivity());
        String username = onBoardingSharedPreferences.getString("Username","");

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS},1);
        }

        binding.welcomeTextView.setText("Hi, " + username);

        contactViewModel.getAll().observe(getViewLifecycleOwner(),contactList -> {
            ContactButtonRecyclerViewAdapter adapter = new ContactButtonRecyclerViewAdapter(getActivity(),contactList);
            binding.contactRecyclerView.setAdapter(adapter);
        });

        layoutManager = new LinearLayoutManager(getActivity().getParent(),LinearLayoutManager.HORIZONTAL,false);
        binding.contactRecyclerView.setLayoutManager(layoutManager);


        ActivityCompat.requestPermissions(requireActivity(),
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        // Add emergency contact
        binding.addContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });

        // FollowMe
        binding.followMe.setOnClickListener(view -> {
            try {
                contactViewModel.getAll().observe(getViewLifecycleOwner(), contactList -> {
                    if (contactList.size() < 1) {
                        throw new RuntimeException("123");
                    }
                });
            }
            catch (RuntimeException e){
                Toast.makeText(requireActivity(), "Please add a contact first!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getActivity(), FollowMeIntroActivity.class);
            startActivity(intent);
        });

        binding.sos.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), SOSActivity.class);
            startActivity(intent);
        });

        binding.searchService.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SafetyTipsIntroActivity.class);
            startActivity(intent);
        });

        binding.dataReport.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), DataReportActivity.class);
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
}