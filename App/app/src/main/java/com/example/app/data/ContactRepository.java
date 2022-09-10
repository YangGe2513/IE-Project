package com.example.app.data;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.app.data.model.Contact;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactDatabase db = ContactDatabase.getInstance(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAll();
    }

    public LiveData<List<Contact>> getAll() {
        return allContacts;
    }

    public void add(final Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.insert(contact);
            }
        });
    }

    public void delete(final Contact contact){
        ContactDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.delete(contact);
            }
        });
    }

    public void updateContact(final Contact contact){
        ContactDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.updateContact(contact);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Contact> findByNameFuture(final String name) {
        return CompletableFuture.supplyAsync(new Supplier<Contact>() {
            @Override
            public Contact get() {
                return contactDao.findByName(name);
            }
        }, ContactDatabase.databaseWriteExecutor);
    }
}

