package com.example.app.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String type;
    @NonNull
    private String time;
    @NonNull
    private String from;
    @NonNull
    private String to;
    @NonNull
    private String contact;
    private String note;

    public Event(@NonNull String type, @NonNull String time, @NonNull String from, @NonNull String to, @NonNull String contact, String note) {
        this.type = type;
        this.time = time;
        this.from = from;
        this.to = to;
        this.contact = contact;
        this.note = note;
    }

    public Event() {

    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public String getFrom() {
        return from;
    }

    public void setFrom(@NonNull String from) {
        this.from = from;
    }

    @NonNull
    public String getTo() {
        return to;
    }

    public void setTo(@NonNull String to) {
        this.to = to;
    }

    @NonNull
    public String getContact() {
        return contact;
    }

    public void setContact(@NonNull String contact) {
        this.contact = contact;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
