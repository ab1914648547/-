package org.develop.attendancesystem.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.develop.attendancesystem.R;
import org.develop.attendancesystem.service.StudentInfoService;
import org.develop.attendancesystem.service.impl.StudentInfoServiceImpl;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "YL-MessageActivity";


    private TextView studentName;
    private TextView studentAge;
    private TextView studentSex;
    private TextView studentPhone;
    private TextView studentHome;
    private TextView studentClass;
    private TextView mQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initView();
        setTextView();
        mQuit.setOnClickListener(this::onClick);
    }

    private void setTextView() {

        SharedPreferences login_message = getSharedPreferences("login_message", MODE_PRIVATE);
        if (login_message != null) {
            String studentId = login_message.getString("studentId", null);
            String classId = login_message.getString("classId", null);
            String classMessage = login_message.getString("classMessage", null);

            if (studentId != null){
                StudentInfoService service = new StudentInfoServiceImpl(this);
                service.getStudentId(studentId, studentInformation -> {
                    if (studentInformation != null){
                        runOnUiThread(() -> {
                            studentName.setText(studentInformation.getStudentName());
                            studentAge.setText(String.valueOf(studentInformation.getStudentAge()) );
                            if ("0".equals(studentInformation.getStudentSex())){
                                studentSex.setText("女");
                            }else {
                                studentSex.setText("男");
                            }
                            studentPhone.setText(studentInformation.getStudentPhone());
                            studentHome.setText(studentInformation.getStudentHome());
                            studentClass.setText(classMessage);
                        });


                    }

                });
            }
        }
    }

    private void initView() {
        mQuit = findViewById(R.id.quit_message);
        studentName = findViewById(R.id.student_name);
        studentAge = findViewById(R.id.student_age);
        studentSex = findViewById(R.id.student_sex);
        studentPhone = findViewById(R.id.student_phone);
        studentHome = findViewById(R.id.student_home);
        studentClass = findViewById(R.id.student_class);
    }

    private void onClick(View view) {
        Intent mIntent = new Intent(this, LoginActivity.class);
        SharedPreferences userInfo = getSharedPreferences("login_message", MODE_PRIVATE);
        SharedPreferences.Editor edit = userInfo.edit();

        edit.putString("studentId", null);
        edit.putString("studentName", null);
        edit.putString("studentClass", null);
        edit.putString("studentHome", null);
        edit.putString("studentPhone", null);
        edit.apply();
        startActivity(mIntent);
        finish();
    }
}