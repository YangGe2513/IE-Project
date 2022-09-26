package com.example.app.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app.MainActivity;
import com.example.app.databinding.FragmentFourthScreenBinding;

public class FourthScreen extends Fragment {
    private FragmentFourthScreenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFourthScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.startAppButton.setOnClickListener(view -> {
            if (binding.nameEditText.getText().toString().isEmpty()){
                binding.textInputLayout.setError("Name cannot be empty!");
                return;
            }
            finishOnBoarding();
        });

        return root;
    }

    private void finishOnBoarding(){
        String username = binding.nameEditText.getText().toString();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NotFinished",false);
        editor.putString("Username",username);
        editor.apply();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}