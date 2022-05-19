package com.example.agenda.ui.activity;

import static com.example.agenda.ui.activity.ConstantsActivities.STUDENTS_LIST_TITLE;
import static com.example.agenda.ui.activity.ConstantsActivities.STUDENT_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.example.agenda.asynctask.RemoveStudentTask;
import com.example.agenda.asynctask.SearchStudentTask;
import com.example.agenda.dao.StudentDAO;
import com.example.agenda.database.AgendaDatabase;
import com.example.agenda.model.Student;
import com.example.agenda.ui.adapter.StudentsListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentsListActivity extends AppCompatActivity {
    private StudentDAO dao;
    private StudentsListAdapter adapter;
    private Student studentSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        setTitle(STUDENTS_LIST_TITLE);

        dao = AgendaDatabase.getInstance(this).getStudentDAO();
        adapter = new StudentsListAdapter(this);
        new SearchStudentTask(dao, adapter).initialRun();
        configButtonAddStudent();
        configListStudents();
    }

    private void configListStudents() {
        ListView students = findViewById(R.id.list_students);
        students.setAdapter(adapter);
        registerForContextMenu(students);
    }

    private void configButtonAddStudent() {
        FloatingActionButton buttonAddStudent = findViewById(R.id.button_add_student);
        buttonAddStudent.setOnClickListener((view) -> startActivity(new Intent(this, FormNewStudentActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SearchStudentTask(dao, adapter).run();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.menu_students_list, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        studentSelected = adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);

        if (item.getItemId() == R.id.menu_remove_student) {
            new AlertDialog.Builder(this).setTitle("Atenção")
                    .setMessage("Tem certeza que quer remover o aluno?")
                    .setPositiveButton("Sim", (dialogInterface, i) -> new RemoveStudentTask(dao, adapter, studentSelected).run())
                    .setNegativeButton("Não", null).show();
        } else if (item.getItemId() == R.id.menu_edit_student) {
            Intent goToForm = new Intent(StudentsListActivity.this, FormNewStudentActivity.class);
            goToForm.putExtra(STUDENT_KEY, studentSelected);
            startActivity(goToForm);
        }

        return super.onContextItemSelected(item);
    }
}