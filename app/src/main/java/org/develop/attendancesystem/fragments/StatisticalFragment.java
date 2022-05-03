package org.develop.attendancesystem.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;


import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.MyInfoBean;
import org.develop.attendancesystem.entity.Signinformation;
import org.develop.attendancesystem.service.CourseInfoService;
import org.develop.attendancesystem.service.SignInfoService;
import org.develop.attendancesystem.service.impl.CourseInfoServiceImpl;
import org.develop.attendancesystem.service.impl.SignInfoServiceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatisticalFragment extends Fragment {
    private final static String TAG = "YL-StatisticalFragment";
    private CalendarView mCalendar;
    private ListView mListView;



    public static StatisticalFragment newInstance(){
        return new StatisticalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistical, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCalendar = view.findViewById(R.id.calendar_show);
        mListView = view.findViewById(R.id.statistical_list);
        new Thread(()->{
            mCalendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {

                disposeClickDate(year, month+1, dayOfMonth);
            });
        }).start();
    }

    private void disposeClickDate(int year, int month, int dayOfMonth) {
        SharedPreferences login_message = Objects.requireNonNull(getContext()).getSharedPreferences("login_message", MODE_PRIVATE);
        if (login_message != null){
            String studentId = login_message.getString("studentId", null);
            SignInfoService signService = new SignInfoServiceImpl(getActivity());
            signService.selectSignInfoStudentId(studentId, year, month, dayOfMonth, signinformations -> {
                if (signinformations!=null){
                    List<MyInfoBean> myInfoBeans = disposeSignData(signinformations);
                    disposeInfoBeans(myInfoBeans);
                }else {
                    mListView.setAdapter(null);
                    MainActivity.showToast(getActivity(),"数据为空！");
                }
            });

        }


    }

    private void disposeInfoBeans(List<MyInfoBean> myInfoBeans) {
        List<MyInfoBean> infoBeans = new ArrayList<>();
        for(MyInfoBean infoBean: myInfoBeans){
            CourseInfoService courseService = new CourseInfoServiceImpl(getActivity());
            courseService.selectCourseInfoId(infoBean.getCourseName(), courseInformation -> {
                infoBean.setCourseName(courseInformation.getCourseName());
                infoBeans.add(infoBean);
            });
        }
        while (true){
            if (infoBeans.size() == myInfoBeans.size()){
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    StatisticalAdapter adapter = new StatisticalAdapter(myInfoBeans, getActivity());
                    mListView.setAdapter(adapter);
                });
                break;
            }
        }
    }

    private List<MyInfoBean> disposeSignData(List<Signinformation> signinformations) {
        List<MyInfoBean> myInfoBeans = new ArrayList<>();
        for (Signinformation signInfo: signinformations){
            MyInfoBean myInfoBean = new MyInfoBean();
            myInfoBean.setCourseName(String.valueOf(signInfo.getCourseId()));
            myInfoBean.setClockTimeState(signInfo.getSignTime() + signInfo.getSignSite());
            if ("true".equals(signInfo.getState())){
                myInfoBean.setDelOrMod("删除");
            }else {myInfoBean.setDelOrMod("修改");}
            myInfoBeans.add(myInfoBean);
        }
        return myInfoBeans;
    }

    private class StatisticalAdapter extends BaseAdapter{
        private List<MyInfoBean> infoBeanList;
        private Context context;
        private LayoutInflater inflater;

        public StatisticalAdapter(List<MyInfoBean> infoBeanList, Context context) {
            this.infoBeanList = infoBeanList;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return infoBeanList!=null ? infoBeanList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return infoBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView != null){
                viewHolder = (ViewHolder) convertView.getTag();
            }else {
                convertView = inflater.inflate(R.layout.item_lists_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.courseName = convertView.findViewById(R.id.course_name);
                viewHolder.clockTimeState = convertView.findViewById(R.id.clock_time_state);
                viewHolder.delOrMod = convertView.findViewById(R.id.del_or_mod);
                convertView.setTag(viewHolder);
            }
            MyInfoBean infoBean = infoBeanList.get(position);

            viewHolder.courseName.setText(infoBean.getCourseName());
            viewHolder.clockTimeState.setText(infoBean.getClockTimeState());
            viewHolder.delOrMod.setText(infoBean.getDelOrMod());

            return convertView;
        }

        public class ViewHolder{
            private TextView courseName;
            private TextView clockTimeState;
            private TextView delOrMod;
        }
    }

}