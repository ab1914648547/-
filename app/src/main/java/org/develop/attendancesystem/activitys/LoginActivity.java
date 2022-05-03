package org.develop.attendancesystem.activitys;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.StudentInformation;
import org.develop.attendancesystem.service.StudentInfoService;
import org.develop.attendancesystem.service.impl.StudentInfoServiceImpl;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "YL-LoginActivity:";

    private SharedPreferences mPref;

    private EditText mUserName;

    private EditText mUserPass;

    private Activity activity = this;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mUserName = findViewById(R.id.user_name);
        mUserPass = findViewById(R.id.user_pass);



        findViewById(R.id.login).setOnClickListener(view -> {
            StudentInfoService studentService = new StudentInfoServiceImpl(activity);
            studentService.getStudentId(mUserName.getText().toString(), this::disposeData);

        });


    }

    private void disposeData(StudentInformation studentInformation){

        Log.e(TAG, "disposeData: "+ studentInformation );

        String username = mUserName.getText().toString();

        String userpass = mUserPass.getText().toString();

        if (studentInformation != null){
            String studentId = "" + studentInformation.getStudentId();

            if (username.equals(studentId) && userpass.equals(studentInformation.getStudentPass())){
                mPref = getSharedPreferences("login_message", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPref.edit();

                editor.putString("studentId", mUserName.getText().toString());
                editor.putString("studentName",studentInformation.getStudentName());
                editor.putString("studentClass",String.valueOf(studentInformation.getClassId()));
                editor.putString("studentHome",studentInformation.getStudentHome());
                editor.putString("studentPhone",studentInformation.getStudentPhone());
                editor.apply();
                finish();
            }else {
                MainActivity.showToast(this, "账号或密码错误！");
            }
        }
    }
}
