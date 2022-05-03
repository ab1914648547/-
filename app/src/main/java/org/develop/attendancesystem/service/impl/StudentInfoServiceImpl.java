package org.develop.attendancesystem.service.impl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.google.gson.Gson;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.StudentInformation;
import org.develop.attendancesystem.service.StudentInfoService;
import org.develop.attendancesystem.utility.OkHttpUtils;

import okhttp3.Call;

public class StudentInfoServiceImpl implements StudentInfoService {

    private final static String TAG = "YL-StudentInfoServiceImpl:";

    private Context mContext;

    private Activity mActivity;

    private Object mRequest = null;

    private StudentInformation studentInformation;

    public StudentInfoServiceImpl(Activity mActivity) {
        this.mContext = mActivity;
        this.mActivity = mActivity;
    }

    @Override
    public void getStudentId(String studentId, Consumer<StudentInformation> consumer) {

        new Thread(() -> {

                String url = mContext.getString(R.string.getIP) + mContext.getString(R.string.getStudentId) + studentId;
                Log.e(TAG, "getStudentId: " + url);
                OkHttpUtils.builder().url(url)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .get().async(new OkHttpUtils.ICallBack() {
                            @Override
                            public void onSuccessful(Call call, String data) {
                                Gson gson = new Gson();
                                StudentInformation studentInformation = gson.fromJson(data, StudentInformation.class);
                                consumer.accept(studentInformation);
                            }

                            @Override
                            public void onFailure(Call call, String errorMsg) {
                                consumer.accept(null);
                                Log.e(TAG, "onFailure: "+ errorMsg );
                                MainActivity.showToast(mActivity, "服务器断掉了！");
                            }
                        });
        }).start();

    }
}
