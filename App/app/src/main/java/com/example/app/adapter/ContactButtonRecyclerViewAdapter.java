package com.example.app.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.data.model.Contact;
import com.example.app.databinding.RecyclerviewContactButtonItemBinding;

import java.util.List;

public class ContactButtonRecyclerViewAdapter extends RecyclerView.Adapter<ContactButtonRecyclerViewAdapter.ViewHolder>{
    private final List<Contact> contacts;
    private final Activity activity;

    public ContactButtonRecyclerViewAdapter(Activity activity, List<Contact> contacts) {
        this.activity = activity;
        this.contacts = contacts;
    }

    private void call(String contactNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + contactNumber);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},1);
        }
        intent.setData(data);
        activity.startActivity(intent);
    }

    @NonNull
    @Override
    public ContactButtonRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewContactButtonItemBinding binding =
                RecyclerviewContactButtonItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactButtonRecyclerViewAdapter.ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();

        holder.binding.nameTextView.setText(name);

        holder.binding.photo.setOnClickListener(view -> {
            call(phoneNumber);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewContactButtonItemBinding binding;

        public ViewHolder(RecyclerviewContactButtonItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
