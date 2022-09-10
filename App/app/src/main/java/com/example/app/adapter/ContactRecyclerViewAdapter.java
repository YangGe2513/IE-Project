package com.example.app.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.ContactDetailActivity;
import com.example.app.data.model.Contact;
import com.example.app.databinding.RecyclerviewContactItemBinding;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {
    private final List<Contact> contacts;
    private final Activity activity;

    public ContactRecyclerViewAdapter(Activity activity, List<Contact> contacts) {
        this.activity = activity;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewContactItemBinding binding =
                RecyclerviewContactItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        String name = contact.getName();
        String phoneNumber = contact.getPhoneNumber();
        String relationship = contact.getRelationship();

        holder.binding.textView3.setText(name);
        holder.binding.PhoneText.setText(phoneNumber);
        holder.binding.relationshipText.setText(relationship);

        holder.binding.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ContactDetailActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("relationship", relationship);
            intent.putExtra("mode", "update");
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, holder.binding.cardView, "a").toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewContactItemBinding binding;

        public ViewHolder(RecyclerviewContactItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
