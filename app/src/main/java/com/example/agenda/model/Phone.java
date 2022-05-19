package com.example.agenda.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Student.class,  parentColumns = "id", childColumns = "studentId", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)})
public class Phone {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long number;
    private PhoneType type;
    private int studentId;

    public Phone(long number, PhoneType type, int studentId) {
        this.number = number;
        this.type = type;
        this.studentId = studentId;
    }

    public Phone(long number, PhoneType type) {
        this.number = number;
        this.type = type;
    }

    public Phone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isEmpty() {
        return number == 0;
    }
}
