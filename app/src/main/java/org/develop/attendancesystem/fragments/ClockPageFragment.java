package org.develop.attendancesystem.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.activitys.ApplyForActivity;
import org.develop.attendancesystem.entity.ClassInformation;
import org.develop.attendancesystem.entity.CourseInformation;
import org.develop.attendancesystem.entity.Signinformation;
import org.develop.attendancesystem.service.ClassInfoService;
import org.develop.attendancesystem.service.CourseInfoService;
import org.develop.attendancesystem.service.SignInfoService;
import org.develop.attendancesystem.service.StudentInfoService;
import org.develop.attendancesystem.service.impl.ClassInfoServiceImpl;
import org.develop.attendancesystem.service.impl.CourseInfoServiceImpl;
import org.develop.attendancesystem.service.impl.SignInfoServiceImpl;
import org.develop.attendancesystem.service.impl.StudentInfoServiceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ClockPageFragment extends Fragment{

    private final static String TAG = "YL-ClockPageFragment";

    private Context mContext;
    private Activity mActivity;
    private TextView mClockText;
    private TextView mClockTime;
    public RelativeLayout mClockThis;
    public TextView mStudentName;
    private TextView studentNote;
    private TextView mBannerTitle;
    private ImageView mApplyFor;
    private TextView mBannerTime;
    private Date parse = null;
    private TextView mLocation;
    private TencentLocationManager mLocationManager;
    private TencentLocationRequest mRequest;
    private Signinformation mSignData = new Signinformation();
    private boolean state = false;
    private String mStrLocation = null;

    ClockHandler mClockHandler = new ClockHandler();
    Message mMessage = new Message();
    public ClockPageFragment(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public static ClockPageFragment newInstance(Context mContext, Activity mActivity) {
        return new ClockPageFragment(mContext, mActivity);
    }

    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock_page_fragment, container, false);

        TencentLocationManager.setUserAgreePrivacy(true);
        initView(view);
        setViewText(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setViewText(View view) {

        mLocationManager = TencentLocationManager.getInstance(getActivity());
        mRequest = TencentLocationRequest.create();
        mRequest.setInterval(1000);
        mRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        mRequest.setAllowDirection(true);
        //是否需要开启室内定位
        mRequest.setIndoorLocationMode(true);

        mLocationManager.requestSingleFreshLocation(mRequest, new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
                Log.e(TAG, "onLocationChanged: tencentLocation= " + tencentLocation + "\n i=" + i + "\n s= "+ s);
                mLocation.setText(tencentLocation.getAddress());
                mSignData.setSignMessage(tencentLocation.getAddress());
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {
                Log.e(TAG, "onLocationChanged: GPS = "+ s +  "\n WiFi=" + i + "\n Cell= "+ s1);
            }
        }, Looper.getMainLooper());



        SharedPreferences login_message = mContext.getSharedPreferences("login_message", MODE_PRIVATE);
        String studentId = login_message.getString("studentId", null);
        if (studentId != null) {
            mSignData.setStudentId(studentId);
            StudentInfoService StudentService = new StudentInfoServiceImpl(mActivity);
            StudentService.getStudentId(studentId, studentInformation -> {
                if (studentInformation != null && studentInformation.getClassId() > 0){
                    mStudentName.setText(studentInformation.getStudentName());
                    long classId = studentInformation.getClassId();

                    CourseInfoService courseService = new CourseInfoServiceImpl(getActivity());
                    courseService.selectCourseInfoClassId(String.valueOf(classId),
                            courseInformation -> {
                                if (courseInformation != null) {
                                    mMessage.what = 0x0003;
                                    mMessage.obj = courseInformation;

                                    Log.e(TAG, "accept: "+courseInformation );

                                    mClockHandler.handleMessage(mMessage);
                                }

                            });



                    ClassInfoService classService = new ClassInfoServiceImpl(mContext);
                    classService.getClassInfoId(String.valueOf(classId), classInformation -> {
                        if (classInformation != null && classInformation.getTeacherId() > 0) {

                            studentNote.setText(classInformation.getClassMessage());
                            SharedPreferences.Editor editor = login_message.edit();
                            editor.putString("classMessage", classInformation.getClassMessage());
                            editor.apply();
                            mMessage.what = 0x0002;
                            mMessage.obj = classInformation;
                            mClockHandler.handleMessage(mMessage);
                        }
                    });

                }else {
                    MainActivity.showToast(mContext,"服务器异常！");
                }
            });
        }else {
            MainActivity.showToast(mContext,"请登录后使用！");
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mMessage.what = 0x0001;
                mClockHandler.handleMessage(mMessage);
            }
        }, 0 , 1000);

        mApplyFor.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ApplyForActivity.class);
            intent.putExtra("studentId",studentId);
            startActivity(intent);
        });

    }

    @SuppressLint("SetTextI18n")
    public void setDate() {

        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);

        String secondStr;
        String minuteStr;
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = String.valueOf(second);
        }


        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = String.valueOf(minute);
        }


        mActivity.runOnUiThread(() -> mClockTime.setText(hour + ":" + minuteStr + ":" + secondStr));
    }

    @SuppressLint("CutPasteId")
    private void initView(View view) {
        mStudentName = view.findViewById(R.id.clock_user_name);
        mClockText = view.findViewById(R.id.clock_text);
        mClockTime = view.findViewById(R.id.clock_time);
        mClockThis = view.findViewById(R.id.clock_this);
        studentNote = view.findViewById(R.id.student_note);
        mBannerTitle = view.findViewById(R.id.banner_title);
        mBannerTime = view.findViewById(R.id.banner_time);
        mLocation = view.findViewById(R.id.location_text);
        mApplyFor = view.findViewById(R.id.apply_for);
    }





    @SuppressLint("HandlerLeak")
    class ClockHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x0001:
                    setDate();

                    break;
                case 0x0002:
                    ClassInformation classInformation = (ClassInformation) msg.obj;
                    Log.e(TAG, "handleMessage: "+ classInformation);

                    break;
                case 0x0003:
                    CourseInformation courseInformation = (CourseInformation) msg.obj;
                    disPoseClockData(courseInformation);
                    break;
            }
        }

        private void disPoseClockData(CourseInformation courseInformation) {
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if (courseInformation != null){
                    parse = dateFormat.parse(courseInformation.getClockTime());
                    Log.e(TAG, "disPoseClockData: "+parse );
                    Calendar calendar = getCalendar(parse);
                    String minuteStr;
                    if (calendar.get(Calendar.MINUTE) < 10) {
                        minuteStr = "0" + calendar.get(Calendar.MINUTE);
                    } else {
                        minuteStr = String.valueOf(calendar.get(Calendar.MINUTE));
                    }
                    String dateTimeShow = "签到时间为[" + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                            + minuteStr + "]之前";
                    mStrLocation = courseInformation.getClockPlace();

                    mSignData.setCourseId(courseInformation.getCourseId());
                    mSignData.setClassId(courseInformation.getClassId());
                    mSignData.setTeacherId(courseInformation.getTeacherId());

                    mBannerTime.setText(dateTimeShow);

                    setBannerTitle(courseInformation);

                }else return;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (parse != null){
                        Calendar nowTimeCalender = Calendar.getInstance();
                        Calendar clockTimeCalender = getCalendar(parse);
                        int nowDate = nowTimeCalender.get(Calendar.YEAR) + (nowTimeCalender.get(Calendar.MONTH) + 1)
                                + nowTimeCalender.get(Calendar.DAY_OF_MONTH);

                        int clockDate = clockTimeCalender.get(Calendar.YEAR) + (clockTimeCalender.get(Calendar.MONTH)+1)
                                + clockTimeCalender.get(Calendar.DAY_OF_MONTH);

                        int nowHour = nowTimeCalender.get(Calendar.HOUR_OF_DAY) * 60 +nowTimeCalender.get(Calendar.MINUTE);
                        int clockHour = clockTimeCalender.get(Calendar.HOUR_OF_DAY) * 60 +clockTimeCalender.get(Calendar.MINUTE);
                        if (((nowHour <= clockHour) && (nowHour >= (clockHour - 10))) && (nowDate == clockDate)){
                            state=true;
                            setTextShowData(mClockText, "打卡上课", 20,R.drawable.clock_btn_shape);

                            if (mStrLocation.equals(mSignData.getSignMessage())){
                                state = true;
                                setTextShowData(mClockText, "打卡上课", 20,R.drawable.clock_btn_shape);
                            }else {
                                state=false;
                                setTextShowData(mClockText, "地点未在范围内", 10, R.drawable.clock_btn_shape_no);
                            }
                        }else {
                            setTextShowData(mClockText, "时间未在范围内", 10, R.drawable.clock_btn_shape_no);
                            state=false;
                        }
//                        setDate();
                    }
                }
            },0,1000);



            getActivity().runOnUiThread(() -> mClockThis.setOnClickListener(v -> {
                new Thread(() -> {
                    try {
                        if(parse != null && state){
                            insertSignData(parse);
                        }else {
                            MainActivity.showToast(mContext, "不是签到时间！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

            }));
        }

        private void insertSignData(Date clockTime) throws Exception {

            Calendar calendar = getCalendar(clockTime);
            String dateTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            mSignData.setSignSite("签到");
            mSignData.setSignTime(dateTime);
            mSignData.setState("true");
            Gson gson = new Gson();
            String signJson = gson.toJson(mSignData);

            Log.e(TAG, "insertSignData: signinformation = "+signJson );

            String url = getString(R.string.getIP) + getString(R.string.insertSignIfo);
            SignInfoService signService = new SignInfoServiceImpl(mActivity);
            signService.insertSignInfo(url, signJson);
//            MainActivity.showToast(getActivity(), "签到成功！");/
        }


        private void setBannerTitle(CourseInformation courseInformation) {
            Map<Integer, String> weekMap = new HashMap<>(7);
            weekMap.put(1, "星期日");
            weekMap.put(2, "星期一");
            weekMap.put(3, "星期二");
            weekMap.put(4, "星期三");
            weekMap.put(5, "星期四");
            weekMap.put(6, "星期五");
            weekMap.put(7, "星期六");
            if (courseInformation!=null){

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date clockDate = dateFormat.parse(courseInformation.getClockTime());
                    Calendar clockTimeData = getCalendar(clockDate);
                    String dateShow = weekMap.get(clockTimeData.get(Calendar.DAY_OF_WEEK) % 7)
                            +" " + courseInformation.getCourseName() +" " + courseInformation.getVenue();
                    getActivity().runOnUiThread(() -> {
                        mBannerTitle.setText(dateShow);
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                MainActivity.showToast(getActivity(), "没有课程！");
            }
        }
    }
    private Calendar getCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    private void setTextShowData(TextView textView, String note, int size, int id) {
        mActivity.runOnUiThread(() -> {
            textView.setText(note);
            textView.setTextSize(size);
            if (id != -1){
                mClockThis.setBackgroundResource(id);
            }
        });

    }
}