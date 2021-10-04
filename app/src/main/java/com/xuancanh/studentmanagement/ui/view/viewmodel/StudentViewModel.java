package com.xuancanh.studentmanagement.ui.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xuancanh.studentmanagement.presentation.room.StudentRepository;
import com.xuancanh.studentmanagement.presentation.model.StudentDTO;

import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository mStudentRepository;
    private final LiveData<List<StudentDTO>> mAllStudents;
    public StudentViewModel(Application application) {
        super(application);
        mStudentRepository = new StudentRepository(application);
        mAllStudents = mStudentRepository.getAllStudents();
    }

    public LiveData<List<StudentDTO>> getAllStudents() {
        return mAllStudents;
    }

    public void insertStudent(StudentDTO student) {
        mStudentRepository.insertStudent(student);
    }

    public void updateStudent(StudentDTO student) {
        mStudentRepository.updateStudent(student);
    }

    public void deleteStudent(StudentDTO student) {
        mStudentRepository.deleteStudent(student);
    }
}
