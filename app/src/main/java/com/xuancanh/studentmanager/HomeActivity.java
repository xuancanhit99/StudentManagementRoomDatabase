package com.xuancanh.studentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xuancanh.studentmanager.database.Database;

public class HomeActivity extends AppCompatActivity {
    Button addStudent, viewStudent, exitApp;

    // initviews
    public void initviews() {
        exitApp = findViewById(R.id.btn_app_exit);
        addStudent = findViewById(R.id.btn_student_add);
        viewStudent = findViewById(R.id.btn_student_view_all);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Call initviews
        initviews();

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

        //Add Student Button
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddActivity.class));
            }
        });
    }
}