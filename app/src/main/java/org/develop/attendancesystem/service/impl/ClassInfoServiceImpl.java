package org.develop.attendancesystem.service.impl;


import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.google.gson.Gson;

import org.develop.attendancesystem.R;
import org.develop.attendancesystem.entity.ClassInformation;
import org.develop.attendancesystem.service.ClassInfoService;
import org.develop.attendancesystem.utility.OkHttpUtils;

public class ClassInfoServiceImpl implements ClassInfoService {

    private static final String TAG = "YL-ClassInfoServiceImpl";

    private Object mRequest = null;
    private ClassInformation classInformation;
    private Context mContext;

    public ClassInfoServiceImpl(Context context) {

        this.mContext = context;
    }

    @Override
    public void getClassInfoId(String classId, Consumer<ClassInformation> consumer) {

        new Thread(() -> {
            String url = mContext.getString(R.string.getIP) + mContext.getString(R.string.getClassId) + classId;
            Log.e(TAG, "run: "+url );
            String classInfo = OkHttpUtils.builder().url(url)
                    .get()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .sync();


            if (!("405".equals(classInfo.substring(0, 3)))){

                Gson gson = new Gson();
                ClassInfoServiceImpl.this.classInformation = gson.fromJson(classInfo, ClassInformation.class);

                if (consumer != null) {
                    consumer.accept(ClassInfoServiceImpl.this.classInformation);
                }
            }else {
                consumer.accept(null);
                Log.e(TAG, "getClassInfoId: " +  classInfo.substring(0, 3));
            }


        }).start();
    }
}
