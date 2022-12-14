package com.example.app.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.adapter.ContactRecyclerViewAdapter;
import com.example.app.data.ContactViewModel;
import com.example.app.databinding.FragmentEmergencyContactBinding;

public class EmergencyContactFragment extends Fragment {

    private FragmentEmergencyContactBinding binding;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEmergencyContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ContactViewModel contactViewModel =
                new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        contactViewModel.getAll().observe(getViewLifecycleOwner(),contactList -> {
            ContactRecyclerViewAdapter adapter = new ContactRecyclerViewAdapter(getActivity(),contactList);
            binding.contactRecyclerView.setAdapter(adapter);
        });

        layoutManager = new LinearLayoutManager(getActivity().getParent());
        binding.contactRecyclerView.setLayoutManager(layoutManager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}