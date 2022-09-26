package com.example.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.data.ContactViewModel;
import com.example.app.data.model.Contact;
import com.example.app.databinding.ActivityContactDetailBinding;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


public class ContactDetailActivity extends AppCompatActivity {

    private ActivityContactDetailBinding binding;
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ContactViewModel.class);

        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        String name = intent.getStringExtra("name");
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String relationship = intent.getStringExtra("relationship");
        binding.nameTextView.setText(name);
        binding.nameEditText.setText(name);
        binding.phoneEditText.setText(phoneNumber);

        binding.saveButton.setOnClickListener(view -> {
            String newName = Objects.requireNonNull(binding.nameEditText.getText()).toString();
            String newPhoneNumber = Objects.requireNonNull(binding.phoneEditText.getText()).toString();
            String newRelationship = binding.relationshipSpinner.getSelectedItem().toString();

            if("update".equals(mode)) {
                updateContact(name,newName,newPhoneNumber,newRelationship);
            }

            if("add".equals(mode)) {
                Contact contact = new Contact(newName, newPhoneNumber, newRelationship);
                addContact(contact);
            }

            finish();
        });

        binding.deleteButton.setOnClickListener(view -> {
            if("update".equals(mode)) {
                delete(name);
            }
            if("add".equals(mode)){
                binding.nameEditText.setText("");
                binding.phoneEditText.setText("");
            }
        });
    }

    // Show a dialog for double check when delete the contact
    private void delete(String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set message
        builder.setMessage("Do you want to delete emergency contact " + name + "?")
                .setTitle("Warning");
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteContact(name);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Delete specific contact
    private void deleteContact(String name){
        // Check version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            CompletableFuture<Contact> contactCompletableFuture = contactViewModel.findByNameFuture(name);
            contactCompletableFuture.thenApply(contact -> {
                if (contact != null) {
                    contactViewModel.delete(contact);
                }
                return contact;
            });
            finish();
        }
    }

    private void updateContact(String name,String newName,String newPhoneNumber,String newRelationship){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            CompletableFuture<Contact> contactCompletableFuture = contactViewModel.findByNameFuture(name);
            contactCompletableFuture.thenApply(contact -> {
                if (contact != null) {
                    contact.setName(newName);
                    contact.setPhoneNumber(newPhoneNumber);
                    contact.setRelationship(newRelationship);
                    contactViewModel.update(contact);
                }
                return contact;
            });
        }
    }

    private void addContact(Contact newContact){
        // Check version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            CompletableFuture<Contact> contactCompletableFuture = contactViewModel.findByNameFuture(newContact.getName());
            contactCompletableFuture.thenApply(contact -> {
                if (contact == null) {
                    contactViewModel.add(newContact);
                }
                return contact;
            });
        }
    }
}