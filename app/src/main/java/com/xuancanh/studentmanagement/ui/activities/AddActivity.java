package com.xuancanh.studentmanagement.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.xuancanh.studentmanagement.domain.model.Student;
import com.xuancanh.studentmanagement.ui.view.viewmodel.StudentViewModel;
import com.xuancanh.studentmanager.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    private EditText edtStuAddName, edtStuAddNo, edtStuAddDOB, edtStuAddPhone, edtStuAddEmail, edtStuAddClass;
    private RadioGroup rgStuAddGender;
    private RadioButton rbStuAddMale, rbStuAddFemale;
    private Button btnStuAddSave, btnStuAddExit, btnStuAddTakePhoto, btnStuAddChoosePhoto, btnStuAddDelDOB;
    private ImageView ivStuAddAvt;
    int addGender = 1;

    String realPath = "";
    Uri imageUri;

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    StudentViewModel studentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Student student = new Student();
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        initUI();

        //Button Choose Photo
        btnStuAddChoosePhoto.setOnClickListener(v -> choosePhoto());

        //Button Take Photo
        btnStuAddTakePhoto.setOnClickListener(v -> takePhoto());

        //Button Exit from Add
        btnStuAddExit.setOnClickListener(v -> finish());

        //Button Save
        btnStuAddSave.setOnClickListener(v -> {
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
        });

        //Button Delete Date of birth
        btnStuAddDelDOB.setOnClickListener(v -> edtStuAddDOB.setText(""));

        //RadioGroup Gender
        rgStuAddGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_stu_add_male) {
                addGender = 1;
            } else {
                addGender = 0;
            }
        });


        //Set click text view Date of birth
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        edtStuAddDOB.setOnClickListener(v -> new DatePickerDialog(AddActivity.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtStuAddDOB.setText(sdf.format(calendar.getTime()));
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
        btnStuAddDelDOB = (Button)findViewById(R.id.btn_stu_add_del_dob);
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
                imageUri = data.getData();
                realPath = getRealPathFromURI(imageUri);
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ivStuAddAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivStuAddAvt.setImageBitmap(bitmap);
                saveToGallery();
                realPath = getRealPathFromURI(imageUri);
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

    // Get Real Path when upload photo(from uri - image/mame_image)
    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void saveToGallery() {
        Bitmap bitmap = ((BitmapDrawable) ivStuAddAvt.getDrawable()).getBitmap();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image From Take Photo");
        values.put(MediaStore.Images.Media.BUCKET_ID, "image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "take photo and save to gallery");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

        //Insert student to database
        studentViewModel.insertStudent(student);

        //After added student move to list
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
        finish();
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