package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.data.ContactViewModel;
import com.example.app.databinding.ActivityContactBinding;


public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactViewModel contactViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).
                        create(ContactViewModel.class);

//        binding.contactRecyclerView.addItemDecoration(new
//                DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        ContactRecyclerViewAdapter adapter = new ContactRecyclerViewAdapter(this,contactViewModel.getAll());
//        binding.contactRecyclerView.setAdapter(adapter);
//        layoutManager = new LinearLayoutManager(getParent());
//        binding.contactRecyclerView.setLayoutManager(layoutManager);
    }
}