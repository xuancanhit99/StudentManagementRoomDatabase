package com.xuancanh.studentmanagement.presentation.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.xuancanh.studentmanagement.presentation.model.StudentDTO;
import com.xuancanh.studentmanagement.presentation.room.dao.StudentDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {StudentDTO.class}, version = 1, exportSchema = false)
public abstract class StudentRoomDatabase extends RoomDatabase {
    public abstract StudentDAO studentDao();
    private static volatile StudentRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static StudentRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StudentRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StudentRoomDatabase.class, "student_database.db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

                StudentDAO dao = INSTANCE.studentDao();
                dao.deleteAllStudents();
                StudentDTO student = new StudentDTO("NAME", "NO", "EMAIL.COM", 1, "XX-XX-XXXX", "00-XX", null, "AAA-VVVV-CCCC");
                dao.insertStudent(student);
                student = new StudentDTO("ANONYMOUS", "ON", "HOTMAIL.COM", 0, "OO-OO-OO", "VV-EE", null, "MMM-AAA-CCC");
                dao.insertStudent(student);
            });
        }
    };

}
