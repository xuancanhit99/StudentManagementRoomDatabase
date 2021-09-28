package com.xuancanh.studentmanager;

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
import android.widget.Toast;

import com.xuancanh.studentmanager.View.Adapters.StudentListAdapter;
import com.xuancanh.studentmanager.Domain.Model.Student;
import com.xuancanh.studentmanager.View.ViewModel.StudentViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {



    private RecyclerView rvItems;
    //private List<Student> studentList;
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


        studentViewModel.getAllStudents().observe(this, new Observer<List<Student>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Student> students) {
                if(students != null) {
                    studentListAdapter = new StudentListAdapter(getApplicationContext(), students);
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