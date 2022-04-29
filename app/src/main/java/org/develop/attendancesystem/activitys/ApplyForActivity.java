package org.develop.attendancesystem.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.CourseInformation;
import org.develop.attendancesystem.entity.Signinformation;
import org.develop.attendancesystem.entity.StudentInformation;
import org.develop.attendancesystem.service.ClassInfoService;
import org.develop.attendancesystem.service.CourseInfoService;
import org.develop.attendancesystem.service.SignInfoService;
import org.develop.attendancesystem.service.StudentInfoService;
import org.develop.attendancesystem.service.TeacherInfoService;
import org.develop.attendancesystem.service.impl.ClassInfoServiceImpl;
import org.develop.attendancesystem.service.impl.CourseInfoServiceImpl;
import org.develop.attendancesystem.service.impl.SignInfoServiceImpl;
import org.develop.attendancesystem.service.impl.StudentInfoServiceImpl;
import org.develop.attendancesystem.service.impl.TeacherInfoServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ApplyForActivity extends AppCompatActivity{

    private static final String TAG = "YL-ApplyForActivity";
    private ApplyHandler mApplyHandler;
    private Context mContext = this;
    private Activity mActivity = this;
    private TextView mStudentName;
    private TextView mStudentClass;
    private Spinner mStudentCourse;
    private Spinner mSpinVacate;
    private Spinner mStudentTeacher;
    private EditText mVacateNote;
    private TextView mSubmitApply;
    private Signinformation signinformation = null;
    private List<String> mCourseIds = null;
    private List<String> mTeacherIds = null;
    private ArrayList<String> mVacate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for);
        signinformation = new Signinformation();
        initView();
        setViewDada();
    }

    private void initView() {
        mApplyHandler = new ApplyHandler();
        mStudentName = findViewById(R.id.student_name);
        mStudentClass = findViewById(R.id.student_class);
        mStudentCourse = findViewById(R.id.student_course);
        mSpinVacate = findViewById(R.id.spinner_vacate);
        mStudentTeacher = findViewById(R.id.spinner_teacher);
        mVacateNote = findViewById(R.id.vacate_note);
        mSubmitApply = findViewById(R.id.submit_apply);
        mCourseIds = new ArrayList<>();
        mTeacherIds = new ArrayList<>();

        mStudentCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signinformation.setCourseId(Long.parseLong(mCourseIds.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mStudentTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signinformation.setTeacherId(Long.parseLong(mTeacherIds.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinVacate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                signinformation.setSignSite(mVacate.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSubmitApply.setOnClickListener(v -> {
            signinformation.setSignMessage(mVacateNote.getText().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String dateTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            signinformation.setSignTime(dateTime);
            signinformation.setState("false");

            SignInfoService signService = new SignInfoServiceImpl(mActivity);
            String url = getString(R.string.getIP) + getString(R.string.insertSignIfo);
            Gson gson = new Gson();
            String json = gson.toJson(signinformation);
            signService.insertSignInfo(url,json);
        });

    }

    private void setViewDada() {
        Intent intent = getIntent();
        String studentId = intent.getStringExtra("studentId");
        if (studentId != null){
            StudentInfoService studentService = new StudentInfoServiceImpl(this);
            studentService.getStudentId(studentId, studentInformation -> {
                if (studentInformation!=null){
                    Message message = new Message();
                    message.obj = studentInformation;
                    message.what = 0x1111;
                    mApplyHandler.handleMessage(message);
                }
            });
        }

        mVacate.add("事假");
        mVacate.add("病假");
        setViewAdapter(mSpinVacate, mVacate);
    }



    class ApplyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x1111:
                    disStudentData((StudentInformation)msg.obj);
                    break;
            }
        }

        private void disStudentData(StudentInformation studentInformation) {

            signinformation.setStudentId(String.valueOf(studentInformation.getStudentId()));
            signinformation.setClassId(studentInformation.getClassId());

            Log.e(TAG, "disStudentData: join------------------->" );
            mStudentName.setText(studentInformation.getStudentName());
            ClassInfoService classService = new ClassInfoServiceImpl(mContext);
            classService.getClassInfoId(String.valueOf(studentInformation.getClassId()), classInformation -> {
                if (classInformation != null){
                    mStudentClass.setText(classInformation.getClassMessage());
                }
            });

            CourseInfoService courseService = new CourseInfoServiceImpl(mActivity);
            courseService.selectCourseInfoClass(String.valueOf(studentInformation.getClassId()), courseInformations -> {
                ArrayList<String> cNameData = new ArrayList<>();
//                List<String> cTeacherIds = new ArrayList<>();
                if (courseInformations!=null){
                    Log.e(TAG, "disStudentData: ------------------->1" + courseInformations );
                    for (CourseInformation cData: courseInformations){
                        cNameData.add(cData.getCourseName());
                        mTeacherIds.add(String.valueOf(cData.getTeacherId()));
                        mCourseIds.add(String.valueOf(cData.getCourseId()));
                    }
                    setViewAdapter(mStudentCourse, cNameData);


                    new Thread(()->{
                        //去除重复的元素
                        for (int i = 0; i< mTeacherIds.size(); i++){
                            for (int j = i+1; j < mTeacherIds.size();){
                                if (mTeacherIds.get(j).equals(mTeacherIds.get(i))){
                                    mTeacherIds.remove(j);
                                    continue;
                                }
                                j++;
                            }
                        }
                        ArrayList<String> teacherNames = new ArrayList<>();
                        AtomicBoolean ifOk = new AtomicBoolean(false);
                        for (String id: mTeacherIds){
                            TeacherInfoService teacherService = new TeacherInfoServiceImpl(mActivity);
                            teacherService.selectTeacherInfoTeacherId(id, teacherInformation -> {
                                if (teacherInformation!=null){
                                    teacherNames.add(teacherInformation.getTeacherName());
                                    Log.e(TAG, "disStudentData: ---------------->2"+teacherInformation);
                                }
                                ifOk.set(true);
                            });
                        }
                        while (true){
                            setViewAdapter(mStudentTeacher, teacherNames);
                            Log.e(TAG, "disStudentData:teacherNames \n"+teacherNames );
                            if (ifOk.get()){
                                break;
                            }
                        }
                    }).start();

                }else {
                    MainActivity.showToast(mContext,"没有课程");
                }
            });
        }

    }
    private void setViewAdapter(Spinner spinner, ArrayList<String> data){
        mActivity.runOnUiThread(() -> {
            ArrayAdapter adapter = new ArrayAdapter(mContext,
                    R.layout.support_simple_spinner_dropdown_item, data);
            spinner.setAdapter(adapter);
        });
    }


}