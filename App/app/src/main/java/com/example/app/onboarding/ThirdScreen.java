package com.example.app.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app.databinding.FragmentThirdScreenBinding;

public class ThirdScreen extends Fragment {

    private FragmentThirdScreenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.getStartButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),ProfileActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}