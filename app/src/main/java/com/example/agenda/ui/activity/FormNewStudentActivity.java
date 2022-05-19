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
import com.example.agenda.asynctask.SaveStudentTask;
import com.example.agenda.asynctask.SearchAllStudentPhones;
import com.example.agenda.dao.PhoneDAO;
import com.example.agenda.dao.StudentDAO;
import com.example.agenda.database.AgendaDatabase;
import com.example.agenda.model.Phone;
import com.example.agenda.model.PhoneType;
import com.example.agenda.model.Student;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class FormNewStudentActivity extends AppCompatActivity {
    private StudentDAO studentDAO;
    private EditText nameField;
    private EditText cellphoneField;
    private EditText telephoneField;
    private EditText emailField;
    private Student student;
    private PhoneDAO phoneDAO;
    private List<Phone> studentPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_student);

        AgendaDatabase database = AgendaDatabase.getInstance(this);
        studentDAO = database.getStudentDAO();
        phoneDAO = database.getPhoneDAO();
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
                emailField.setText(student.getEmail());
                new SearchAllStudentPhones(this, phoneDAO, student, (phones) -> {
                    studentPhones = phones;
                    for (Phone phone : phones) {
                        if (phone.getType() == PhoneType.TELEPHONE) telephoneField.setText(String.valueOf(phone.getNumber()));
                        else cellphoneField.setText(String.valueOf(phone.getNumber()));
                    }
                }).run();
            }
        } else {
            setTitle(FORM_NEW_STUDENT_ADD_TITLE);
            student = new Student();
        }
    }

    private void initializeForm() {
        nameField = ((TextInputLayout) findViewById(R.id.input_name)).getEditText();
        cellphoneField = ((TextInputLayout) findViewById(R.id.input_cellphone)).getEditText();
        telephoneField = ((TextInputLayout) findViewById(R.id.input_telephone)).getEditText();
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
            String telephoneFieldText = !telephoneField.getText().toString().equals("") ? telephoneField.getText().toString() : "0";
            String cellphoneFieldText = !cellphoneField.getText().toString().equals("") ? cellphoneField.getText().toString() : "0";
            student.setName(nameField.getText().toString());
            student.setEmail(emailField.getText().toString());

            if (student.hasValidId()) {
                new SaveStudentTask(this, studentDAO, phoneDAO, student, telephoneFieldText, cellphoneFieldText, studentPhones, this::finish).runEdit();
            } else {
                new SaveStudentTask(this, studentDAO, phoneDAO, student, telephoneFieldText, cellphoneFieldText, studentPhones, this::finish).runSaveFromZero();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}