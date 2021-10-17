package com.xuancanh.studentmanagement.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.xuancanh.studentmanagement.domain.model.Student;
import com.xuancanh.studentmanagement.ui.tools.DividerItemDecorator;
import com.xuancanh.studentmanager.R;
import com.xuancanh.studentmanagement.ui.view.adapters.StudentListAdapter;
import com.xuancanh.studentmanagement.presentation.model.StudentDTO;
import com.xuancanh.studentmanagement.ui.view.viewmodel.StudentViewModel;

import java.util.Arrays;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {


    private RecyclerView rvItems;
    private StudentListAdapter studentListAdapter;

    ImageButton ibStuAdd;
    StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);


        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        //Circle Button Add
        ibStuAdd = findViewById(R.id.ib_stu_add);
        ibStuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewAllActivity.this, AddActivity.class));
            }
        });

        addControlsAndReadData();
    }

    private void addControlsAndReadData() {
        rvItems = (RecyclerView) findViewById(R.id.rv_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(ViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Get data All Students to Adapter
        studentViewModel.getAllStudents().observe(this, new Observer<List<StudentDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<StudentDTO> students) {
                if (students != null) {
                    // Data from List(Live data StudentDTO from table sqlite in device) just only read
                    // Want cover to Student for show data to View(Adapter using Student) then Update
                    List<Student> studentList = Arrays.asList(new Student[students.size()]);
                    for (int i = 0; i < students.size(); i++) {
                        studentList.set(i, StudentDTO.convertFromStudentDTO(students.get(i)));
                    }
                    studentListAdapter = new StudentListAdapter(getApplicationContext(), studentList);
                    rvItems.setAdapter(studentListAdapter);
                }
                studentListAdapter.notifyDataSetChanged();
            }
        });

        //Fix E/RecyclerView: No adapter attached; skipping layout
        studentListAdapter = new StudentListAdapter(getApplicationContext(), null); // this
        rvItems.setAdapter(studentListAdapter);
    }

}