package com.example.agenda.asynctask;

import com.example.agenda.dao.StudentDAO;
import com.example.agenda.ui.adapter.StudentsListAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchStudentTask {
    private final ExecutorService service = Executors.newFixedThreadPool(1);
    private final StudentDAO dao;
    private final StudentsListAdapter adapter;

    public SearchStudentTask(StudentDAO dao, StudentsListAdapter adapter) {
        this.dao = dao;
        this.adapter = adapter;
    }

    public void initialRun() {
        service.execute(() -> {
            try {
                adapter.update(dao.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void run() {
        service.execute(() -> {
            try {
                adapter.update(dao.getAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }
}
