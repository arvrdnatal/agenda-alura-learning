package com.example.agenda.asynctask;

import android.content.Context;

import com.example.agenda.dao.PhoneDAO;
import com.example.agenda.database.AgendaDatabase;
import com.example.agenda.model.Phone;
import com.example.agenda.model.Student;
import com.example.agenda.ui.activity.StudentsListActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangePhoneFieldStudentsListTask {
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final PhoneDAO dao;
    private final Context context;
    private final Student student;
    private final FirstPhoneFoundListener listener;
    private Phone phone;

    public ChangePhoneFieldStudentsListTask(Context context, Student student, FirstPhoneFoundListener listener) {
        this.dao = AgendaDatabase.getInstance(context).getPhoneDAO();
        this.context = context;
        this.student = student;
        this.listener = listener;
    }

    public void run() {
        service.execute(() -> {
            try {
                phone = dao.searchStudentFirstPhone(student.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((StudentsListActivity) context).runOnUiThread(() -> {
                if (phone != null) listener.whenWasFound(phone);
            });
        });
        service.shutdown();
    }

    public interface FirstPhoneFoundListener {
        void whenWasFound(Phone phoneFound);
    }
}
