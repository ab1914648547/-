package org.develop.attendancesystem.service.impl;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.CourseInformation;
import org.develop.attendancesystem.entity.TeacherInformation;
import org.develop.attendancesystem.service.TeacherInfoService;
import org.develop.attendancesystem.utility.OkHttpUtils;

import java.util.function.Consumer;

import okhttp3.Call;

public class TeacherInfoServiceImpl implements TeacherInfoService {

    private Activity activity;

    public TeacherInfoServiceImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void selectTeacherInfoTeacherId(String teacherId, Consumer<TeacherInformation> consumer) {
//        new TeacherThread(() -> {
            String url = activity.getString(R.string.getIP) + activity.getString(R.string.selectTeacherInfoTeacherId)
                    +teacherId;
            OkHttpUtils.builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .get()
                    .async(new OkHttpUtils.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String data) {
                            Gson gson = new Gson();
                            TeacherInformation teacherInformation = gson.fromJson(data, TeacherInformation.class);
                            consumer.accept(teacherInformation);
                        }

                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            consumer.accept(null);
                        }
                    });
//        }).start();
    }

    class TeacherThread extends Thread{
        public TeacherThread(@Nullable Runnable target) {
            super(target);
        }

        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.run();
        }

        @Override
        public synchronized void start() {
            super.start();
        }
    }
}
