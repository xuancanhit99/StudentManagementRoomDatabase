package com.xuancanh.studentmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xuancanh.studentmanager.adapter.StudentAdapter;
import com.xuancanh.studentmanager.database.Database;
import com.xuancanh.studentmanager.model.Student;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    final String DATABASE_NAME = "stuDB.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;


    //Anh xa
    private EditText edtStuUpdateName, edtStuUpdateNo, edtStuUpdateDOB, edtStuUpdatePhone, edtStuUpdateEmail, edtStuUpdateClass;
    private RadioGroup rgStuUpdateGender;
    private RadioButton rbStuUpdateMale, rbStuUpdateFemale;
    private Button btnStuUpdateSave, btnStuUpdateExit, btnStuUpdateDelete, btnStuUpdateTakePhoto, btnStuUpdateChoosePhoto;
    private ImageView ivStuUpdateAvt;
    int updateGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //Get data from key STUDENT_DATA push to student
        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra("STUDENT_DATA");


        //Anh xa
        initUI();

        //Get data from student and push to View
        pushDataToView(student);

        //Button Choose Photo
        btnStuUpdateChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnStuUpdateTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit from Update
        btnStuUpdateExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Save
        btnStuUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(student);
                Toast.makeText(UpdateActivity.this,"Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        //Button Delete
        btnStuUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete Student");
                builder.setMessage("Are you sure delete student " + student.getStu_name()+"?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(student);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //RadioGroup Gender
        rgStuUpdateGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_stu_update_male) {
                    updateGender = 1;
                }
                else {
                    updateGender = 0;
                }
            }
        });
    }

    private void delete(Student student) {
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.delete("students", "StudentId = ?", new String[] {student.getStu_id() + ""});
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

    private void pushDataToView(Student student) {
        edtStuUpdateName.setText(student.getStu_name());
        edtStuUpdateNo.setText(student.getStu_no());
        edtStuUpdateDOB.setText(student.getStu_dob());
        edtStuUpdatePhone.setText(student.getStu_phone());
        edtStuUpdateEmail.setText(student.getStu_email());
        edtStuUpdateClass.setText(student.getStu_class());

        if (student.getStu_gender() == 1) {
            rbStuUpdateMale.setChecked(true);
            updateGender = 1;
        }
        else {
            rbStuUpdateFemale.setChecked(true);
            updateGender = 0;
        }

        //Show ImageView
        if(student.getStu_avt() != null) {
            Bitmap bmAvt = BitmapFactory.decodeByteArray(student.getStu_avt(), 0,student.getStu_avt().length);
            ivStuUpdateAvt.setImageBitmap(bmAvt);
        }
        else {
            if(student.getStu_gender() == 1) {
                ivStuUpdateAvt.setImageResource(R.drawable.male);
            }
            else if(student.getStu_gender() == 0) {
                ivStuUpdateAvt.setImageResource(R.drawable.female);
            }
            else {
                ivStuUpdateAvt.setImageResource(R.drawable.graduated);
            }
        }
    }

    private void initUI() {
        edtStuUpdateName = (EditText) findViewById(R.id.edt_stu_update_name);
        edtStuUpdateNo = (EditText) findViewById(R.id.edt_stu_update_no);
        edtStuUpdateDOB = (EditText) findViewById(R.id.edt_stu_update_dob);
        edtStuUpdatePhone = (EditText) findViewById(R.id.edt_stu_update_phone);
        edtStuUpdateEmail = (EditText) findViewById(R.id.edt_stu_update_email);
        edtStuUpdateClass = (EditText) findViewById(R.id.edt_stu_update_class);

        rgStuUpdateGender = (RadioGroup) findViewById(R.id.rg_stu_update_gender);
        rbStuUpdateFemale = (RadioButton) findViewById(R.id.rb_stu_update_female);
        rbStuUpdateMale = (RadioButton) findViewById(R.id.rb_stu_update_male);
        btnStuUpdateSave = (Button) findViewById(R.id.btn_stu_update_save);
        btnStuUpdateExit = (Button) findViewById(R.id.btn_stu_update_exit);
        btnStuUpdateDelete = (Button) findViewById(R.id.btn_stu_update_delete);
        btnStuUpdateTakePhoto = (Button) findViewById(R.id.btn_stu_update_take_photo);
        btnStuUpdateChoosePhoto = (Button) findViewById(R.id.btn_stu_update_choose_photo);
        ivStuUpdateAvt = (ImageView) findViewById(R.id.iv_stu_update_avt);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivStuUpdateAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                ivStuUpdateAvt.setImageBitmap(bitmap);
            }
        }
    }

    private byte[] getByteArrayFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //Update to database
    private void update(Student student) {
        student.setStu_name(edtStuUpdateName.getText().toString());
        student.setStu_no(edtStuUpdateNo.getText().toString());
        student.setStu_dob(edtStuUpdateDOB.getText().toString());
        student.setStu_phone(edtStuUpdatePhone.getText().toString());
        student.setStu_email(edtStuUpdateEmail.getText().toString());
        student.setStu_class(edtStuUpdateClass.getText().toString());
        student.setStu_gender(updateGender);
        student.setStu_avt(getByteArrayFromImageView(ivStuUpdateAvt));

        ContentValues contentValues = new ContentValues();
        contentValues.put("StudentName", student.getStu_name());
        contentValues.put("StudentNo", student.getStu_no());
        contentValues.put("StudentEmail", student.getStu_email());
        contentValues.put("StudentGender", student.getStu_gender());
        contentValues.put("StudentDOB", student.getStu_dob());
        contentValues.put("StudentClass", student.getStu_class());
        contentValues.put("StudentAvatar", student.getStu_avt());
        contentValues.put("StudentPhone", student.getStu_phone());

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.update("students", contentValues, "StudentId = ?", new String[] {student.getStu_id() + ""});

        //finish();
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }
}