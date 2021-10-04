package com.xuancanh.studentmanagement.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuancanh.studentmanager.R;


public class HomeActivity extends AppCompatActivity {
    Button addStudent, viewStudent, exitApp;


    public void initView() {
        exitApp = findViewById(R.id.btn_app_exit);
        addStudent = findViewById(R.id.btn_student_add);
        viewStudent = findViewById(R.id.btn_student_view_all);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Call initView
        initView();

        //Exit Button
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //View All Button
        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ViewAllActivity.class));
            }
        });

        //Add StudentDTO Button
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddActivity.class));
            }
        });
    }
}