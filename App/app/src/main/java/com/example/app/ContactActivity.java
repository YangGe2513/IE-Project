package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.adapter.ContactRecyclerViewAdapter;
import com.example.app.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contactRecyclerView.addItemDecoration(new
                DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        ContactRecyclerViewAdapter adapter = new ContactRecyclerViewAdapter(this);
        binding.contactRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getParent());
        binding.contactRecyclerView.setLayoutManager(layoutManager);
    }
}