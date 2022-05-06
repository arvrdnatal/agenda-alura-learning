package com.example.agenda.database.converter;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class ConversorCalendar {

    @TypeConverter
    public Long toLong(Calendar value) {
        return value.getTimeInMillis();
    }

    @TypeConverter
    public Calendar toCalendar(Long value) {
        Calendar now = Calendar.getInstance();
        if (value != null) now.setTimeInMillis(value);
        return now;
    }
}
