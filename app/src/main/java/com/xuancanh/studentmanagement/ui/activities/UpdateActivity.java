package com.xuancanh.studentmanagement.ui.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.xuancanh.studentmanagement.domain.model.Student;
import com.xuancanh.studentmanager.R;

import com.xuancanh.studentmanagement.ui.view.viewmodel.StudentViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateActivity extends AppCompatActivity {
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;


    private EditText edtStuUpdateName, edtStuUpdateNo, edtStuUpdateDOB, edtStuUpdatePhone, edtStuUpdateEmail, edtStuUpdateClass;
    private RadioGroup rgStuUpdateGender;
    private RadioButton rbStuUpdateMale, rbStuUpdateFemale;
    private Button btnStuUpdateSave, btnStuUpdateExit, btnStuUpdateDelete, btnStuUpdateTakePhoto, btnStuUpdateChoosePhoto, btnStuUpdateDelDOB;
    private ImageView ivStuUpdateAvt;
    int updateGender;

    //for date of birth
    final Calendar calendar = Calendar.getInstance();

    StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

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
                Intent intent = new Intent(UpdateActivity.this, ViewAllActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Button Save
        btnStuUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmptyEditText(edtStuUpdateName) && isEmptyEditText(edtStuUpdateNo)) {
                    edtStuUpdateNo.setError("Please enter student's №");
                    edtStuUpdateName.setError("Please enter student's name");
                }
                else if(isEmptyEditText(edtStuUpdateNo)) {
                    edtStuUpdateNo.setError("Please enter student's №");
                }
                else if(isEmptyEditText(edtStuUpdateName)) {
                    edtStuUpdateName.setError("Please enter student's name");
                }
                else {
                    if(isEmailValid(edtStuUpdateEmail)) {
                        update(student);
                        Toast.makeText(UpdateActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        edtStuUpdateEmail.setError("Email address not valid");
                    }
                }
            }
        });

        //Button Delete
        btnStuUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setIcon(R.drawable.ic_baseline_delete_24);
                builder.setTitle("Delete Student");
                builder.setMessage("Are you sure delete student " + student.getStu_name() + "?");
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

        //Button Delete Date of birth
        btnStuUpdateDelDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtStuUpdateDOB.setText("");
            }
        });

        //RadioGroup Gender
        rgStuUpdateGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_stu_update_male) {
                    updateGender = 1;
                } else {
                    updateGender = 0;
                }
            }
        });

        //Set click text view Date of birth
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        edtStuUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //Label for date of birth
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtStuUpdateDOB.setText(sdf.format(calendar.getTime()));
    }

    private void delete(Student student) {
        studentViewModel.deleteStudent(student);
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
        } else {
            rbStuUpdateFemale.setChecked(true);
            updateGender = 0;
        }

        //Show ImageView
        if (student.getStu_avt() != null) {
            Bitmap bmAvt = BitmapFactory.decodeByteArray(student.getStu_avt(), 0, student.getStu_avt().length);
            ivStuUpdateAvt.setImageBitmap(bmAvt);
        } else {
            if (student.getStu_gender() == 1) {
                ivStuUpdateAvt.setImageResource(R.drawable.male);
            } else if (student.getStu_gender() == 0) {
                ivStuUpdateAvt.setImageResource(R.drawable.female);
            } else {
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
        btnStuUpdateDelDOB = (Button)findViewById(R.id.btn_stu_update_del_dob);
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
    public void onBackPressed() {
        Intent intent = new Intent(UpdateActivity.this, ViewAllActivity.class);
        startActivity(intent);
        finish();
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
                    ivStuUpdateAvt.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivStuUpdateAvt.setImageBitmap(bitmap);
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

    //Update to database
    private void update(Student student) {

        student.setStu_name(edtStuUpdateName.getText().toString());
        student.setStu_no(edtStuUpdateNo.getText().toString());
        student.setStu_dob(edtStuUpdateDOB.getText().toString());
        student.setStu_phone(edtStuUpdatePhone.getText().toString());
        student.setStu_email(edtStuUpdateEmail.getText().toString());
        student.setStu_class(edtStuUpdateClass.getText().toString());
        student.setStu_gender(updateGender);


        //Compare imageView with image
        final ImageView imageViewAvt = (ImageView) findViewById(R.id.iv_stu_update_avt);
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
            student.setStu_avt(getByteArrayFromImageView(ivStuUpdateAvt));
        }

        //Update student to database
        studentViewModel.updateStudent(student);

        Intent intent = new Intent(UpdateActivity.this, ViewAllActivity.class);
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