package org.develop.attendancesystem.service.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.Signinformation;
import org.develop.attendancesystem.fragments.ClockPageFragment;
import org.develop.attendancesystem.service.SignInfoService;
import org.develop.attendancesystem.utility.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.Call;

public class SignInfoServiceImpl implements SignInfoService {
    private static final String TAG = "YL-SignInfoServiceImpl";

    private Activity mActivity;
    public SignInfoServiceImpl(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void insertSignInfo(String url, String json) {

        new Thread(()->{
            OkHttpUtils.builder().url(url)
                    .post(true, json)
                    .async(new OkHttpUtils.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String data) {

                            if ("true".equals(data)){
                                MainActivity.showToast(mActivity, "签到成功！");
                            }else {
                                MainActivity.showToast(mActivity, "已经签到或请假！");
                            }

//                                disPose(data);
                        }

                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            MainActivity.showToast(mActivity,"服务器异常！");
//                            Toast.makeText(activity, "error:" + errorMsg,Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: errorMsg = " + errorMsg + " call = " + call );
                        }
                    });
        }).start();
    }

    @Override
    public void selectSignInfoStudentId(String studentId, int year, int month, int day, Consumer<List<Signinformation>> consumer) {
        String url = mActivity.getString(R.string.getIP)
                + mActivity.getString(R.string.selectSignInfoStudent);
        OkHttpUtils.builder().url(url)
                .addParam("studentId",studentId)
                .addParam("year", String.valueOf(year))
                .addParam("month", String.valueOf(month))
                .addParam("day", String.valueOf(day))
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .get()
                .async(new OkHttpUtils.ICallBack() {
                    @Override
                    public void onSuccessful(Call call, String data) {
                        if (!"null".equals(data)){
                            List<Signinformation> signinformations = JSONArray.parseArray(data, Signinformation.class);
                            consumer.accept(signinformations);
                        }else consumer.accept(null);
                    }

                    @Override
                    public void onFailure(Call call, String errorMsg) {
                        consumer.accept(null);
                    }
                });


    }


    public static class SignThread extends Thread implements Runnable{

        private String url;
        private String json;
        private Activity activity;
        private boolean getOrPost = false;
        private String data = null;

        public SignThread(String url, String json, Activity activity, boolean getOrPost) {
            super();
            this.url = url;
            this.json = json;
            this.activity = activity;
            this.getOrPost = getOrPost;
        }

        public SignThread(String url,  Activity activity, boolean getOrPost) {
            super();
            this.url = url;
            this.json = json;
            this.activity = activity;
            this.getOrPost = getOrPost;
        }

        @Override
        public synchronized void start() {
            super.start();

        }

        @Override
        public void run() {
            super.run();
            Log.e(TAG, "insertSignInfo: 3>>>>>>>>>>>>>>>>"+json );


            if (getOrPost){

            }else {

            }

        }

        public void disPose(String data){
            this.data = data;

            activity.runOnUiThread(() -> {
                if ("true".equals(data)){
                    Toast.makeText(activity, "签到成功！", Toast.LENGTH_SHORT).show();
                    ClockPageFragment clockPageFragment = ClockPageFragment.newInstance(activity, activity);
                    clockPageFragment.mClockThis = activity.findViewById(R.id.clock_this);
                    clockPageFragment.mClockThis.setBackgroundResource(R.drawable.edit_shape);

                }else {
                    Toast.makeText(activity, "签到失败，网络错误！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
