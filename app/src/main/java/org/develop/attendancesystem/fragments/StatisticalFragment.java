package org.develop.attendancesystem.fragments;

import static android.content.Context.MODE_PRIVATE;

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
    private List<MyInfoBean> myInfoBeans;


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
                myInfoBeans = new ArrayList<>();
                disposeClickDate(year, month, dayOfMonth);
            });
        }).start();
    }

    private void disposeClickDate(int year, int month, int dayOfMonth) {
        SharedPreferences login_message = Objects.requireNonNull(getContext()).getSharedPreferences("login_message", MODE_PRIVATE);
        if (login_message != null){
            String studentId = login_message.getString("studentId", null);
            SignInfoService signService = new SignInfoServiceImpl(getActivity());
            signService.selectSignInfoStudentId(studentId, signinformations -> {
                if (signinformations!=null){
                    List<MyInfoBean> myInfoBeans2 = disposeInfoBeans(disposeSignData(signinformations, year, month, dayOfMonth));

                    getActivity().runOnUiThread(() -> {
                        StatisticalAdapter adapter = new StatisticalAdapter(myInfoBeans2, getActivity());
                        mListView.setAdapter(adapter);
                    });
                }
            });

        }


    }

    private List<MyInfoBean> disposeInfoBeans(List<MyInfoBean> myInfoBeans1) {
        AtomicBoolean isOk = new AtomicBoolean(false);
        for(int i = 0; i<myInfoBeans1.size(); i++){
            CourseInfoService courseService = new CourseInfoServiceImpl(getActivity());
            int finalI = i;
            courseService.selectCourseInfoId(myInfoBeans1.get(i).getCourseName(), courseInformation -> {
                myInfoBeans1.get(finalI).setCourseName(courseInformation.getCourseName());
                isOk.set(true);
            });
        }
        while (true){
            if (isOk.get()){
                return myInfoBeans1;
            }
        }
    }

    private List<MyInfoBean> disposeSignData(List<Signinformation> signinformations,int year, int month, int dayOfMonth) {
        MyInfoBean myInfoBean = new MyInfoBean();
        int day = year + month + dayOfMonth;
        for (Signinformation signInfo: signinformations){
            try {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse = dateFormat.parse(signInfo.getSignTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parse);
                int getDay = calendar.get(Calendar.YEAR)
                        + calendar.get(Calendar.MONTH)
                        + calendar.get(Calendar.DAY_OF_MONTH);

                Log.e(TAG, "disposeSignData: 时间1= "+year + month + dayOfMonth + " 时间2 = " + calendar.get(Calendar.YEAR)
                        + calendar.get(Calendar.MONTH)
                        + calendar.get(Calendar.DAY_OF_MONTH) );

                if (day == getDay){
                    myInfoBean.setCourseName(String.valueOf(signInfo.getCourseId()));
                    myInfoBean.setClockTimeState(signInfo.getSignTime() + signInfo.getSignSite());
                    if ("true".equals(signInfo.getState())){
                        myInfoBean.setDelOrMod("删除");
                    }else myInfoBean.setDelOrMod("修改");
                    myInfoBeans.add(myInfoBean);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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