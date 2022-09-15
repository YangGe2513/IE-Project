package com.example.app.ui.home;

import android.Manifest;
import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.ContactDetailActivity;
import com.example.app.MapsActivity;
import com.example.app.R;
import com.example.app.SmartCallActivity;
import com.example.app.adapter.ContactButtonRecyclerViewAdapter;
import com.example.app.data.ContactViewModel;
import com.example.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ContactViewModel contactViewModel;
    private RecyclerView.LayoutManager layoutManager;
    private MediaPlayer mediaPlayer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Load data
        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        contactViewModel.getAll().observe(getViewLifecycleOwner(),contactList -> {
            ContactButtonRecyclerViewAdapter adapter = new ContactButtonRecyclerViewAdapter(getActivity(),contactList);
            binding.contactRecyclerView.setAdapter(adapter);
        });

        layoutManager = new LinearLayoutManager(getActivity().getParent(),LinearLayoutManager.HORIZONTAL,false);
        binding.contactRecyclerView.setLayoutManager(layoutManager);


        // FollowMe
        binding.followMe.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });

        // SmartCall
        binding.fakeVoice.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SmartCallActivity.class);
            startActivity(intent);
        });

//        // Emergency call 1
//        binding.callButton1.setOnClickListener(view -> {
//            String contact1 = sharedPreferences.getString("contact1", "");
//            call(contact1);
//        });
//
//        // Emergency call 2
//        binding.callButton2.setOnClickListener(view -> {
//            String contact2 = sharedPreferences.getString("contact2", "");
//            call(contact2);
//        });

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

        binding.sosButton.setOnClickListener(view->{
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
}