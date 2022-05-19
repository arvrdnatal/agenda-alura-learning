package com.example.agenda.asynctask;

import android.content.Context;

import com.example.agenda.dao.PhoneDAO;
import com.example.agenda.model.Phone;
import com.example.agenda.model.Student;
import com.example.agenda.ui.activity.FormNewStudentActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchAllStudentPhones {
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final Context context;
    private final PhoneDAO phoneDAO;
    private final Student student;
    private final PhonesFoundListener listener;
    private List<Phone> studentPhones;

    public SearchAllStudentPhones(Context context, PhoneDAO phoneDAO, Student student, PhonesFoundListener listener) {
        this.context = context;
        this.phoneDAO = phoneDAO;
        this.student = student;
        this.listener = listener;
    }

    public void run() {
        service.execute(() -> {
            try {
                studentPhones = phoneDAO.searchAllStudentPhone(student.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((FormNewStudentActivity) context).runOnUiThread(() -> listener.whenWasFound(studentPhones));
        });
        service.shutdown();
    }

    public interface PhonesFoundListener {
        void whenWasFound(List<Phone> phones);
    }
}
