package com.xuancanh.studentmanagement.presentation.room;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xuancanh.studentmanagement.domain.model.Student;
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

    public void insertStudent(Student student) {
        StudentDTO dto = StudentDTO.convertFromStudent(student);
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDAO.insertStudent(dto)
        );
    }

    public void updateStudent(Student student) {
        StudentDTO dto = StudentDTO.convertFromStudent(student);
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDAO.updateStudent(dto));
    }

    public void deleteStudent(Student student) {
        StudentDTO dto = StudentDTO.convertFromStudent(student);
        StudentRoomDatabase.databaseWriteExecutor.execute(() ->
                mStudentDAO.deleteStudent(dto));
    }


}
