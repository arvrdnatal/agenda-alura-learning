package com.example.agenda.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.agenda.model.Phone;

import java.util.List;

@Dao
public interface PhoneDAO {
    @Insert
    void save(Phone phone);

    @Query("SELECT * FROM Phone WHERE studentId = :id LIMIT 1")
    Phone searchStudentFirstPhone(int id);

    @Query("SELECT * FROM Phone WHERE studentId = :id")
    List<Phone> searchAllStudentPhone(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Phone phone);

    @Delete
    void delete(Phone phone);
}
