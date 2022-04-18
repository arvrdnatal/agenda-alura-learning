package com.example.agenda.dao;

import com.example.agenda.model.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentDAO {
    private final static Map<Integer, Student> students = new HashMap<>();
    private static int counterIds = 1;

    public void save(Student student) {
        student.setId(counterIds);
        students.put(counterIds, student);
        counterIds++;
    }

    public void edit(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
        }
    }

    public Map<Integer, Student> getAll() {
        return new HashMap<>(students);
    }

    public void remove(Student student) {
        students.remove(student.getId());
    }
}
