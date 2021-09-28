package com.xuancanh.studentmanager.Presentation;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.xuancanh.studentmanager.Domain.Model.Student;
import com.xuancanh.studentmanager.Presentation.DAO.StudentDao;


import java.util.List;

public class StudentRepository {
    private StudentDao mStudentDao;
    private LiveData<List<Student>> mAllStudents;

    public StudentRepository(Application application) {
        StudentRoomDatabase db = StudentRoomDatabase.getDatabase(application);
        mStudentDao = db.studentDao();
        mAllStudents = mStudentDao.getAlphabetizedAllStudents();
    }
     public LiveData<List<Student>> getAllStudents() {
        return mAllStudents;
     }

     public void insertStudent(Student student){
       StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDao.insertStudent(student)
        );
     }

     public void updateStudent(Student student) {
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDao.updateStudent(student));
     }

    public void deleteStudent(Student student) {
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDao.deleteStudent(student));
    }
}
