package com.example.agenda.asynctask;

import android.content.Context;

import com.example.agenda.dao.PhoneDAO;
import com.example.agenda.dao.StudentDAO;
import com.example.agenda.model.Phone;
import com.example.agenda.model.PhoneType;
import com.example.agenda.model.Student;
import com.example.agenda.ui.activity.FormNewStudentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SaveStudentTask {
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private final Context context;
    private final StudentDAO studentDAO;
    private final PhoneDAO phoneDAO;
    private final Student student;
    private final String telephoneFieldText;
    private final String cellphoneFieldText;
    private final List<Phone> studentPhones;
    private final SaveStudentListener listener;
    private Long id;
    private Phone telephone;
    private Phone cellphone;

    public SaveStudentTask(Context context, StudentDAO studentDAO, PhoneDAO phoneDAO, Student student, String telephoneFieldText, String cellphoneFieldText, List<Phone> studentPhones, SaveStudentListener listener) {
        this.context = context;
        this.studentDAO = studentDAO;
        this.phoneDAO = phoneDAO;
        this.student = student;
        this.telephoneFieldText = telephoneFieldText;
        this.cellphoneFieldText = cellphoneFieldText;
        this.studentPhones = studentPhones;
        this.listener = listener;
    }

    public void runSaveFromZero() {
        service.execute(() -> {
            try {
                id = studentDAO.save(student);
                telephone = new Phone(Long.parseLong(telephoneFieldText), PhoneType.TELEPHONE, id.intValue());
                if (!telephone.isEmpty()) phoneDAO.save(telephone);
                cellphone = new Phone(Long.parseLong(cellphoneFieldText), PhoneType.CELLPHONE, id.intValue());
                if (!cellphone.isEmpty()) phoneDAO.save(cellphone);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((FormNewStudentActivity) context).runOnUiThread(listener::whenWasSaved);
        });
        service.shutdown();
    }

    public void runEdit() {
        service.execute(() -> {
            try {
                telephone = new Phone(Long.parseLong(telephoneFieldText), PhoneType.TELEPHONE, student.getId());
                cellphone = new Phone(Long.parseLong(cellphoneFieldText), PhoneType.CELLPHONE, student.getId());
                studentDAO.update(student);

                if (studentPhones.isEmpty()) {
                    if (!telephone.isEmpty()) phoneDAO.save(telephone);
                    if (!cellphone.isEmpty()) phoneDAO.save(cellphone);
                }

                List<PhoneType> contains = new ArrayList<>();
                for (Phone phone : studentPhones) {
                    if (phone.getType() == PhoneType.TELEPHONE) {
                        if (telephone.isEmpty()) phoneDAO.delete(phone);
                        else {
                            telephone.setId(phone.getId());
                            phoneDAO.update(telephone);
                            contains.add(telephone.getType());
                        }
                    } else {
                        if (cellphone.isEmpty()) phoneDAO.delete(phone);
                        else {
                            cellphone.setId(phone.getId());
                            phoneDAO.update(cellphone);
                            contains.add(cellphone.getType());
                        }
                    }
                }

                if (!contains.contains(telephone.getType()) && !telephone.isEmpty()) phoneDAO.save(telephone);
                if (!contains.contains(cellphone.getType()) && !cellphone.isEmpty()) phoneDAO.save(cellphone);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((FormNewStudentActivity) context).runOnUiThread(listener::whenWasSaved);
        });
        service.shutdown();
    }

    public interface SaveStudentListener {
        void whenWasSaved();
    }
}