package com.xuancanh.studentmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xuancanh.studentmanager.adapter.StudentAdapter;
import com.xuancanh.studentmanager.database.Database;
import com.xuancanh.studentmanager.model.Student;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {

    //Database
    final String DATABASE_NAME = "stuDB.db";
    SQLiteDatabase database;

    //
    private RecyclerView rvItems;
    private ArrayList<Student> studentArrayList;
    private StudentAdapter studentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);


        addControls();
        readData();

//        //Test
//        //Data
//        studentArrayList = new ArrayList<Student>();
//        Student st1 = new Student(1, "Xuan Canh", "L10", "xuancanhit99@gmail.com", 1, "14-09-1999", "IKBO-07-19", "89858944005");
//        studentArrayList.add(st1);
//        Student st2 = new Student(2, "Xuan Canh 1", "L11", "xqwet99@gmail.com", 0, "11-09-1999", "IKBO-07-20", "898123124005");
//        studentArrayList.add(st2);
//        Student st3 = new Student(3, "Xuan Canh 2", "L12", "xuadsd@gmail.com", 1, "14-12-1999", "IKBO-04-19", "89858123015");
//        studentArrayList.add(st3);
//        Student st4 = new Student(4, "Xuan Canh 3", "L13", "qwenhit99@gmail.com", 1, "14-09-1987", "IKBO-05-19", "89851234005");
//        studentArrayList.add(st4);
//        Student st5 = new Student(5, "Xuan Canh 4", "L14", "xuancanee@gmail.com", 0, "14-01-2000", "IKBO-01-18", "89851234005");
//        studentArrayList.add(st5);
//
//        //Anh xa
//        rvItems = (RecyclerView)findViewById(R.id.rv_items);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        rvItems.setLayoutManager(layoutManager);
//        rvItems.setHasFixedSize(true);
//
//
//        //
//        studentAdapter = new StudentAdapter(getApplicationContext(),studentArrayList);
//        rvItems.setAdapter(studentAdapter);
    }



    private void addControls() {
        studentArrayList = new ArrayList<Student>();

        rvItems = (RecyclerView)findViewById(R.id.rv_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        studentAdapter = new StudentAdapter(getApplicationContext(),studentArrayList); // this
        rvItems.setAdapter(studentAdapter);



    }

    private void readData() {
        //Call check and cre database
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM students", null);
        studentArrayList.clear();
        for(int i=0; i<cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            //colum first = StudentId, second = StudentName... in database
            int s_id = cursor.getInt(0);
            String s_name = cursor.getString(1);
            String s_no = cursor.getString(2);
            String s_email = cursor.getString(3);
            int s_gender = cursor.getInt(4);
            String s_dob = cursor.getString(5);
            String s_class = cursor.getString(6);
            byte[] s_avt = cursor.getBlob(7);
            String s_phone = cursor.getString(8);
            studentArrayList.add(new Student(s_id, s_name, s_no, s_email, s_gender, s_dob, s_class, s_avt, s_phone));
        }
        studentAdapter.notifyDataSetChanged();
    }

}