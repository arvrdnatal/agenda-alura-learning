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

import com.example.agenda.dao.StudentDAO;
import com.example.agenda.database.converter.ConversorCalendar;
import com.example.agenda.model.Student;

@Database(entities = {Student.class}, version = 4, exportSchema = false)
@TypeConverters({ConversorCalendar.class})
public abstract class AgendaDatabase extends RoomDatabase {
    public abstract StudentDAO getStudentDAO();
    public static AgendaDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AgendaDatabase.class, DATABASE_NAME)
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE student ADD COLUMN lastname TEXT");
                    }
                }, new Migration(2, 3) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        // para remover tabela é necessário:
                        // criar nova tabela
                        database.execSQL(
                                "CREATE TABLE IF NOT EXISTS temp_student" +
                                        "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                        "name TEXT," +
                                        "phone INTEGER NOT NULL," +
                                        "email TEXT)"
                        );
                        // copiar os dados da tabela antiga
                        database.execSQL("INSERT INTO temp_student (id, name, phone, email)" +
                                "SELECT id, name, phone, email FROM student");
                        // remover tabela antiga
                        database.execSQL("DROP TABLE student");
                        // renomear a tabela nova com o nome da antiga
                        database.execSQL("ALTER TABLE temp_student RENAME TO student");
                    }
                }, new Migration(3, 4) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE student ADD COLUMN createdOn INTEGER");
                    }
                })
                .allowMainThreadQueries().build();
    }
}
