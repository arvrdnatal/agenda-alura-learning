package com.example.agenda;

import android.app.Application;

import com.example.agenda.dao.StudentDAO;
import com.example.agenda.model.Student;

public class AgendaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StudentDAO dao = new StudentDAO();
        dao.save(new Student("Teste", "48123456789", "teste@"));
        dao.save(new Student("Alow", "48123456789", "email@"));
        dao.save(new Student("Eae", "48123456789", "eae@"));
        dao.save(new Student("Alowe", "48123456789", "alowe@"));
        dao.save(new Student("Alowie", "48123456789", "alowie@"));
    }
}
