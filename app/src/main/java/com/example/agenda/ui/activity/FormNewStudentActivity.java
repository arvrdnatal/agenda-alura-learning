package com.example.agenda.ui.activity;

import static com.example.agenda.ui.activity.ConstantsActivities.FORM_NEW_STUDENT_ADD_TITLE;
import static com.example.agenda.ui.activity.ConstantsActivities.FORM_NEW_STUDENT_EDIT_TITLE;
import static com.example.agenda.ui.activity.ConstantsActivities.STUDENT_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.example.agenda.dao.StudentDAO;
import com.example.agenda.model.Student;
import com.google.android.material.textfield.TextInputLayout;

public class FormNewStudentActivity extends AppCompatActivity {
    private final StudentDAO dao = new StudentDAO();
    private EditText nameField;
    private EditText phoneField;
    private EditText emailField;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_student);

        initializeForm();
        setValues();
    }

    private void setValues() {
        Intent prevIntent = getIntent();

        if (prevIntent.hasExtra(STUDENT_KEY)) {
            setTitle(FORM_NEW_STUDENT_EDIT_TITLE);
            student = (Student) prevIntent.getSerializableExtra(STUDENT_KEY);
            if (student != null) {
                nameField.setText(student.getName());
                phoneField.setText(student.getPhone());
                emailField.setText(student.getEmail());
            }
        } else {
            setTitle(FORM_NEW_STUDENT_ADD_TITLE);
            student = new Student();
        }
    }

    private void initializeForm() {
        nameField = ((TextInputLayout) findViewById(R.id.input_name)).getEditText();
        phoneField = ((TextInputLayout) findViewById(R.id.input_phone)).getEditText();
        emailField = ((TextInputLayout) findViewById(R.id.input_email)).getEditText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form_new_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save_student) {
            student.setName(nameField.getText().toString());
            student.setPhone(phoneField.getText().toString());
            student.setEmail(emailField.getText().toString());

            if (student.hasValidId()) dao.edit(student);
            else dao.save(student);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}