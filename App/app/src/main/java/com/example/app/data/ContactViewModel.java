package com.example.app.data;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app.data.model.Contact;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository cRepository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel(Application application) {
        super(application);
        cRepository = new ContactRepository(application);
        allContacts = cRepository.getAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Contact> findByNameFuture(final String name) {
        return cRepository.findByNameFuture(name);
    }

    public LiveData<List<Contact>> getAll() {
        return allContacts;
    }

    public void add(Contact contact) {
        cRepository.add(contact);
    }

    public void delete(Contact contact) {
        cRepository.delete(contact);
    }

    public void update(Contact contact) {
        cRepository.updateContact(contact);
    }
}
