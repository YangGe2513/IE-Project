package com.example.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app.data.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    LiveData<List<Contact>> getAll();

    @Query("SELECT * FROM contact WHERE name = :name LIMIT 1")
    Contact findByName(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Update
    void updateContact(Contact contact);

    @Delete
    void delete(Contact contact);
}
