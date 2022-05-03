package org.develop.attendancesystem.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.develop.attendancesystem.MainActivity;
import org.develop.attendancesystem.R;
import org.develop.attendancesystem.activitys.LoginActivity;
import org.develop.attendancesystem.activitys.MessageActivity;
import org.develop.attendancesystem.service.StudentInfoService;
import org.develop.attendancesystem.service.impl.StudentInfoServiceImpl;

import java.io.Serializable;

public class MyMsgFragment extends Fragment {

    private static final String TAG = "YL-MyMsgFragment:";
    private Intent mIntent;
    public boolean mLoginState = false;
    public TextView mUserMyName;
    private TextView mUserNote;
    private RelativeLayout mUserMsg;

    public static MyMsgFragment newInstance() {
        return new MyMsgFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_message_fragment, container, false);
        RelativeLayout mUserMsg = view.findViewById(R.id.user_msg);
        mUserMyName = view.findViewById(R.id.user_MyName);
        mUserNote = view.findViewById(R.id.user_note);

        SharedPreferences login_message = getActivity().getSharedPreferences("login_message", MODE_PRIVATE);
        String studentId = login_message.getString("studentId", null);
        String classMessage = login_message.getString("classMessage", null);



        StudentInfoService service = new StudentInfoServiceImpl(getActivity());
        service.getStudentId(studentId, studentInformation -> {
            if (studentInformation == null) {
                mLoginState = false;
                mIntent = new Intent(MyMsgFragment.this.getActivity(), LoginActivity.class);
            } else {
                mLoginState = true;
                mIntent = new Intent(MyMsgFragment.this.getActivity(), MessageActivity.class);
                String name = studentInformation.getStudentName();
                if (!TextUtils.isEmpty(name)) {
                    mUserMyName.setText(name);
                    mUserNote.setText(classMessage);
                }
            }
        });

        mUserMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIntent == null) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(mIntent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
