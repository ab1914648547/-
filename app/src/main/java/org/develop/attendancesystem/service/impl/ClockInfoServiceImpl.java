package org.develop.attendancesystem.service.impl;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.entity.ClockInfomation;
import org.develop.attendancesystem.service.ClockInfoService;
import org.develop.attendancesystem.utility.OkHttpUtils;

import java.util.List;
import java.util.function.Consumer;

import okhttp3.Call;

public class ClockInfoServiceImpl implements ClockInfoService {

    private static final String TAG = "YL-ClockInfoServiceImpl";

    private Activity activity;
    public ClockInfoServiceImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void selectClockInfo(String url,List<String> param, Consumer<List<ClockInfomation>> consumer) {
        Log.e(TAG, "selectClockInfo: join>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        new Thread(() -> {

            OkHttpUtils.builder().url(url)
                    .addParam("classId",param.get(0))
                    .addParam("teacherId",param.get(1))
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .get()
                    .async(new OkHttpUtils.ICallBack() {
                        @Override
                        public void onSuccessful(Call call, String data) {
                            Log.e(TAG, "onSuccessful: " + data);
                            List<ClockInfomation> clockInfomationList = JSONArray.parseArray(data, ClockInfomation.class);
                            consumer.accept(clockInfomationList);
                        }

                        @Override
                        public void onFailure(Call call, String errorMsg) {
                            MainActivity.showToast(activity, "服务器异常！");
                        }
                    });
        }).start();
    }


    class ClockThread extends Thread implements Runnable{
        public ClockThread() {
            super();
        }
        public ClockThread(@Nullable Runnable target) {
            super(target);
        }
        @Override
        public synchronized void start() {
            super.start();
        }

        @Override
        public void run() {

        }
    }
}
