package com.xuancanh.studentmanagement.presentation.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.xuancanh.studentmanagement.presentation.room.dao.StudentDAO;
import com.xuancanh.studentmanagement.presentation.model.StudentDTO;


import java.util.List;

public class StudentRepository {
    private StudentDAO mStudentDAO;
    private LiveData<List<StudentDTO>> mAllStudents;

    public StudentRepository(Application application) {
        StudentRoomDatabase db = StudentRoomDatabase.getDatabase(application);
        mStudentDAO = db.studentDao();
        mAllStudents = mStudentDAO.getAlphabetizedAllStudents();
    }
     public LiveData<List<StudentDTO>> getAllStudents() {
        return mAllStudents;
     }

     public void insertStudent(StudentDTO student){
       StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDAO.insertStudent(student)
        );
     }

     public void updateStudent(StudentDTO student) {
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDAO.updateStudent(student));
     }

    public void deleteStudent(StudentDTO student) {
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDAO.deleteStudent(student));
    }
}
