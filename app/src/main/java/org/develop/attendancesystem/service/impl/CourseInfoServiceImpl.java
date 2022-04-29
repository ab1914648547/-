package org.develop.attendancesystem.service.impl;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.ClockInfomation;
import org.develop.attendancesystem.entity.CourseInformation;
import org.develop.attendancesystem.service.CourseInfoService;
import org.develop.attendancesystem.utility.OkHttpUtils;

import java.util.List;
import java.util.function.Consumer;

import okhttp3.Call;

public class CourseInfoServiceImpl implements CourseInfoService {

    private static final String TAG = "YL-CourseInfoServiceImpl";
    private Activity activity;

    public CourseInfoServiceImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void selectCourseInfoClassId(String classId, Consumer<CourseInformation> consumer) {
        new CourseThread(() -> {
            String url = activity.getString(R.string.getIP)+activity.getString(R.string.selectCourseInfoClassId)
                    + classId;
            if (url!=null){
                Log.e(TAG, "selectCourseInfoId: "+ url);
                OkHttpUtils.builder()
                        .url(url)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .get()
                        .async(new OkHttpUtils.ICallBack() {
                            @Override
                            public void onSuccessful(Call call, String data) {
                                Gson gson = new Gson();
                                CourseInformation courseInformation = gson.fromJson(data,CourseInformation.class);;
//                                List<CourseInformation> courseInformations = JSONArray.parseArray(data, CourseInformation.class);
                                consumer.accept(courseInformation);
                            }

                            @Override
                            public void onFailure(Call call, String errorMsg) {
                                MainActivity.showToast(activity,"服务器异常！");
                                consumer.accept(null);
                            }
                        });
            }


        }).start();

    }

    @Override
    public void selectCourseInfoClass(String classId, Consumer<List<CourseInformation>> consumer) {
        new CourseThread(() -> {
            String url = activity.getString(R.string.getIP)+activity.getString(R.string.selectCourseInfoClass)
                    + classId;

            OkHttpUtils.builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .get()
                    .async(new OkHttpUtils.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String data) {
                            List<CourseInformation> courseInformations = JSONArray.parseArray(data, CourseInformation.class);
                            consumer.accept(courseInformations);
                        }

                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            consumer.accept(null);
                        }
                    });

        }).start();
    }

    @Override
    public void selectCourseInfoId(String courseId, Consumer<CourseInformation> consumer) {
        new CourseThread(() -> {
            String url = activity.getString(R.string.getIP)+activity.getString(R.string.selectCourseInfoId)
                    + courseId;
            OkHttpUtils.builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .get()
                    .async(new OkHttpUtils.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String data) {
                            Gson gson = new Gson();
                            CourseInformation courseInformation = gson.fromJson(data, CourseInformation.class);
                            consumer.accept(courseInformation);
                        }

                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            consumer.accept(null);
                        }
                    });
        }).start();
    }



    class CourseThread extends Thread{

        public CourseThread(@Nullable Runnable target) {
            super(target);
        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public synchronized void start() {
            super.start();
        }
    }
}
