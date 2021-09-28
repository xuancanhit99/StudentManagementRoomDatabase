package com.xuancanh.studentmanager.View.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xuancanh.studentmanager.Domain.Model.Student;
import com.xuancanh.studentmanager.Presentation.StudentRepository;

import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository mStudentRepository;
    private final LiveData<List<Student>> mAllStudents;
    public StudentViewModel(Application application) {
        super(application);
        mStudentRepository = new StudentRepository(application);
        mAllStudents = mStudentRepository.getAllStudents();
    }

    public LiveData<List<Student>> getAllStudents() {
        return mAllStudents;
    }

    public void insertStudent(Student student) {
        mStudentRepository.insertStudent(student);
    }

    public void updateStudent(Student student) {
        mStudentRepository.updateStudent(student);
    }

    public void deleteStudent(Student student) {
        mStudentRepository.deleteStudent(student);
    }
}
