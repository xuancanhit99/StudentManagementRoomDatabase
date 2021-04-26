package com.xuancanh.studentmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xuancanh.studentmanager.database.Database;
import com.xuancanh.studentmanager.model.Student;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {
    final String DATABASE_NAME = "stuDB.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    //Anh xa
    private EditText edtStuAddName, edtStuAddNo, edtStuAddDOB, edtStuAddPhone, edtStuAddEmail, edtStuAddClass;
    private RadioGroup rgStuAddGender;
    private RadioButton rbStuAddMale, rbStuAddFemale;
    private Button btnStuAddSave, btnStuAddExit, btnStuAddTakePhoto, btnStuAddChoosePhoto;
    private ImageView ivStuAddAvt;
    int addGender = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Student student = new Student();

        //Anh xa
        initUI();

        //Button Choose Photo
        btnStuAddChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        //Button Take Photo
        btnStuAddTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        //Button Exit from Add
        btnStuAddExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button Save
        btnStuAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyEditText(edtStuAddName) && isEmptyEditText(edtStuAddNo)) {
                    edtStuAddNo.setError("Please enter student's №");
                    edtStuAddName.setError("Please enter student's name");
                }
                else if(isEmptyEditText(edtStuAddNo)) {
                    edtStuAddNo.setError("Please enter student's №");
                }
                else if(isEmptyEditText(edtStuAddName)) {
                    edtStuAddName.setError("Please enter student's name");
                }
                else {
                    if(isEmailValid(edtStuAddEmail)) {
                        insert(student);
                        Toast.makeText(AddActivity.this, "Added Student " + student.getStu_name() + " Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        edtStuAddEmail.setError("Email address not valid");
                    }
                }
            }
        });

        //RadioGroup Gender
        rgStuAddGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_stu_add_male) {
                    addGender = 1;
                } else {
                    addGender = 0;
                }
            }
        });
    }

    private void initUI() {
        edtStuAddName = (EditText) findViewById(R.id.edt_stu_add_name);
        edtStuAddNo = (EditText) findViewById(R.id.edt_stu_add_no);
        edtStuAddDOB = (EditText) findViewById(R.id.edt_stu_add_dob);
        edtStuAddPhone = (EditText) findViewById(R.id.edt_stu_add_phone);
        edtStuAddEmail = (EditText) findViewById(R.id.edt_stu_add_email);
        edtStuAddClass = (EditText) findViewById(R.id.edt_stu_add_class);
        rgStuAddGender = (RadioGroup) findViewById(R.id.rg_stu_add_gender);
        rbStuAddFemale = (RadioButton) findViewById(R.id.rb_stu_add_female);
        rbStuAddMale = (RadioButton) findViewById(R.id.rb_stu_add_male);
        btnStuAddSave = (Button) findViewById(R.id.btn_stu_add_save);
        btnStuAddExit = (Button) findViewById(R.id.btn_stu_add_exit);
        btnStuAddTakePhoto = (Button) findViewById(R.id.btn_stu_add_take_photo);
        btnStuAddChoosePhoto = (Button) findViewById(R.id.btn_stu_add_choose_photo);
        ivStuAddAvt = (ImageView) findViewById(R.id.iv_stu_add_avt);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivStuAddAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivStuAddAvt.setImageBitmap(bitmap);
            }
        }
    }

    private byte[] getByteArrayFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //Insert to database
    private void insert(Student student) {
        student.setStu_name(edtStuAddName.getText().toString());
        student.setStu_no(edtStuAddNo.getText().toString());
        student.setStu_dob(edtStuAddDOB.getText().toString());
        student.setStu_phone(edtStuAddPhone.getText().toString());
        student.setStu_email(edtStuAddEmail.getText().toString());
        student.setStu_class(edtStuAddClass.getText().toString());
        student.setStu_gender(addGender);

        //Compare imageView with image
        final ImageView imageViewAvt = (ImageView) findViewById(R.id.iv_stu_add_avt);
        final Bitmap bitmap = ((BitmapDrawable) imageViewAvt.getDrawable()).getBitmap();
        Drawable myDrawableFemale = getResources().getDrawable(R.drawable.female);
        Drawable myDrawableMale = getResources().getDrawable(R.drawable.male);
        Drawable myDrawableGraduated = getResources().getDrawable(R.drawable.graduated);
        final Bitmap myPhotoDefaultFemale = ((BitmapDrawable) myDrawableFemale).getBitmap();
        final Bitmap myPhotoDefaultMale = ((BitmapDrawable) myDrawableMale).getBitmap();
        final Bitmap myPhotoDefaultGraduated = ((BitmapDrawable) myDrawableGraduated).getBitmap();
        if (bitmap.sameAs(myPhotoDefaultFemale) || bitmap.sameAs(myPhotoDefaultMale) || bitmap.sameAs(myPhotoDefaultGraduated)) {
            student.setStu_avt(null);
        } else {
            student.setStu_avt(getByteArrayFromImageView(ivStuAddAvt));
        }

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
        database.insert("students", null, contentValues);

        //After added student move to list
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

    private boolean isEmptyEditText(EditText editText) {
        String str = editText.getText().toString();
        if(TextUtils.isEmpty(str)) {
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString();
        if(email.equals("")) return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}