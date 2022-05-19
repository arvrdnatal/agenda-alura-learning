package com.example.agenda.asynctask;

import com.example.agenda.dao.StudentDAO;
import com.example.agenda.model.Student;
import com.example.agenda.ui.adapter.StudentsListAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoveStudentTask {
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final StudentDAO dao;
    private final StudentsListAdapter adapter;
    private final Student student;

    public RemoveStudentTask(StudentDAO dao, StudentsListAdapter adapter, Student student) {
        this.dao = dao;
        this.adapter = adapter;
        this.student = student;
    }

    public void run() {
        service.execute(() -> {
            try {
                dao.remove(student);
                adapter.remove(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }
}
