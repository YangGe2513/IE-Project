package com.example.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app.data.model.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE id = :id LIMIT 1")
    Event findById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Update
    void updateContact(Event event);

    @Delete
    void delete(Event event);
}
