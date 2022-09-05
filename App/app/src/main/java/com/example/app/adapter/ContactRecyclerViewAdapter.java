package com.example.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.ContactDetailActivity;
import com.example.app.databinding.RecyclerviewContactItemBinding;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>{

    private Activity activity;

    public ContactRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewContactItemBinding binding=
                RecyclerviewContactItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ViewHolder holder, int position) {
//        holder.binding.propertyInfoTextView.setText("123");
        holder.binding.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactDetailActivity.class);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewContactItemBinding binding;
        public ViewHolder(RecyclerviewContactItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
