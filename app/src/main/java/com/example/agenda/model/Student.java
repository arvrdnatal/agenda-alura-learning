package com.example.agenda.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Entity
public class Student implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String name;
    private String email;
    private Calendar createdOn = Calendar.getInstance();

    @Ignore
    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Calendar getCreatedOn() {
        return createdOn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    public boolean hasValidId() {
        return id > 0;
    }

    public String getFullName() {
        return name;
    }

    public String getCreationDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return formatter.format(createdOn.getTime());
    }
}
