package com.example.agenda.database;

import static com.example.agenda.ui.activity.ConstantsActivities.DATABASE_NAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.agenda.dao.PhoneDAO;
import com.example.agenda.dao.StudentDAO;
import com.example.agenda.database.converter.ConversorCalendar;
import com.example.agenda.database.converter.ConversorPhoneType;
import com.example.agenda.model.Phone;
import com.example.agenda.model.PhoneType;
import com.example.agenda.model.Student;

@Database(entities = {Student.class, Phone.class}, version = 6, exportSchema = false)
@TypeConverters({ConversorCalendar.class, ConversorPhoneType.class})
public abstract class AgendaDatabase extends RoomDatabase {
    public abstract StudentDAO getStudentDAO();
    public abstract PhoneDAO getPhoneDAO();
    public static AgendaDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AgendaDatabase.class, DATABASE_NAME)
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE Student ADD COLUMN lastname TEXT");
                    }
                }, new Migration(2, 3) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        // para remover tabela é necessário:
                        // criar nova tabela
                        database.execSQL(
                                "CREATE TABLE IF NOT EXISTS temp_student " +
                                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        "name TEXT, " +
                                        "phone INTEGER NOT NULL, " +
                                        "email TEXT)"
                        );
                        // copiar os dados da tabela antiga
                        database.execSQL("INSERT INTO temp_student (id, name, phone, email) " +
                                "SELECT id, name, phone, email FROM student");
                        // remover tabela antiga
                        database.execSQL("DROP TABLE Student");
                        // renomear a tabela nova com o nome da antiga
                        database.execSQL("ALTER TABLE temp_student RENAME TO Student");
                    }
                }, new Migration(3, 4) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE Student ADD COLUMN createdOn INTEGER");
                    }
                }, new Migration(4, 5) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS temp_student (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "name TEXT, " +
                                "phone INTEGER NOT NULL, " +
                                "telephone INTEGER NOT NULL, " +
                                "email TEXT, " +
                                "createdOn INTEGER)");
                        database.execSQL("INSERT INTO temp_student (id, name, phone, telephone, email, createdOn) " +
                                "SELECT id, name, phone, telephone, email, createdOn FROM student");
                        database.execSQL("DROP TABLE Student");
                        database.execSQL("ALTER TABLE temp_student RENAME TO Student");
                    }
                }, new Migration(5, 6) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS temp_student (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "name TEXT, " +
                                "email TEXT, " +
                                "createdOn INTEGER)");
                        database.execSQL("INSERT INTO temp_student (id, name, email, createdOn) " +
                                "SELECT id, name, email, createdOn FROM student");
                        database.execSQL("CREATE TABLE IF NOT EXISTS Phone (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "number INTEGER NOT NULL, " +
                                "type TEXT, " +
                                "studentId INTEGER NOT NULL, " +
                                "FOREIGN KEY(studentId) REFERENCES Student(id) ON UPDATE CASCADE ON DELETE CASCADE )");
                        database.execSQL("UPDATE Phone SET type = ?", new PhoneType[] {PhoneType.TELEPHONE});
                        database.execSQL("DROP TABLE Student");
                        database.execSQL("ALTER TABLE temp_student RENAME TO Student");
                    }
                })
                .build();
    }
}
