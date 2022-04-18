package com.example.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agenda.R;
import com.example.agenda.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentsListAdapter extends BaseAdapter {
    private final Context context;
    private List<Student> students;

    public StudentsListAdapter(Context context, Map<Integer, Student> students) {
        this.context = context;
        this.students = new ArrayList<>(students.values());
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Student getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        Student student = students.get(position);
        if (student != null) return student.getId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_students_list, parent, false);
        } else {
            view = convertView;
        }

        TextView name = view.findViewById(R.id.text_name);
        TextView phone = view.findViewById(R.id.text_phone);
        TextView email = view.findViewById(R.id.text_email);

        Student student = students.get(position);
        if (student != null) {
            name.setText(student.getName());
            phone.setText(student.getPhone());
            email.setText(student.getEmail());
        }

        return view;
    }

    private void clear() {
        students.clear();
    }

    private void addAll(Map<Integer, Student> allStudents) {
        students = new ArrayList<>(allStudents.values());
    }

    public void update(Map<Integer, Student> allStudents) {
        clear();
        addAll(allStudents);
        notifyDataSetChanged();
    }

    public void remove(Student student) {
        students.remove(student);
        notifyDataSetChanged();
    }
}
