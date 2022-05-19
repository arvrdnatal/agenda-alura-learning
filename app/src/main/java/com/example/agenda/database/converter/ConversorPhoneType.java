package com.example.agenda.database.converter;

import androidx.room.TypeConverter;

import com.example.agenda.model.PhoneType;

public class ConversorPhoneType {

    @TypeConverter
    public String toString(PhoneType type) {
        return type.name();
    }

    @TypeConverter
    public PhoneType toPhoneType(String type) {
        if (type != null) return PhoneType.valueOf(type);
        return null;
    }
}
