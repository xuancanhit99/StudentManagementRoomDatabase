package com.xuancanh.studentmanager.Presentation.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xuancanh.studentmanager.Domain.Model.Student;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStudent(Student student);

    @Update
    void updateStudent(Student student);

    @Query("UPDATE student_table SET stuName = :stu_name, stuNo = :stu_no, stuEmail = :stu_email, stuGender = :stu_gender, stuDob = :stu_dob, stuClass = :stu_class, stuAvt = :stu_avt, stuPhone = :stu_phone WHERE stuId =:stu_id")
    void update(String stu_name, String stu_no, String stu_email, int stu_gender, String stu_dob, String stu_class, byte[] stu_avt, String stu_phone, int stu_id);

    @Delete
    void deleteStudent(Student student);

    @Query("DELETE FROM student_table")
    void deleteAllStudents();

    @Query("SELECT * FROM student_table ORDER BY stuName ASC")
    LiveData<List<Student>> getAlphabetizedAllStudents();
}
