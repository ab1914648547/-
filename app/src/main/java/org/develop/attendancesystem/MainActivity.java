package org.develop.attendancesystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.develop.attendancesystem.fragments.ClockPageFragment;
import org.develop.attendancesystem.fragments.MyMsgFragment;
import org.develop.attendancesystem.fragments.StatisticalFragment;
import org.develop.attendancesystem.utility.Permission;
import org.develop.attendancesystem.utility.PermissionDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "YL-MainActivity";

    private static final int CLOCK_PAGE = 0;
    private static final int STATISTICAL_PAGE = 1;
    private static final int MESSAGE_PAGE = 2;
    private List<Fragment> lists;
    private ViewPager mViewPager;
    private TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setViewPageShow();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission.REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    showPermissionDenyDialog();
                    return;
                }
            }
        }

    }
    private void showPermissionDenyDialog() {
        PermissionDialog dialog = new PermissionDialog();
        dialog.show(getFragmentManager(), "PermissionDeny");
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Permission.checkPermission(this);
    }

    private void setViewPageShow() {
        int currentItem = mViewPager.getCurrentItem();

        switch (currentItem) {
            case CLOCK_PAGE:
                mTitleText.setText(R.string.clock_str);
                break;
            case STATISTICAL_PAGE:
                mTitleText.setText(R.string.statistics_str);
                break;
            case MESSAGE_PAGE:
                mTitleText.setText(R.string.my_str);
                break;
            default:
                break;
        }
    }

    private void initView() {

        ClockPageFragment clockPageFragment = ClockPageFragment
                .newInstance(this, this);
        StatisticalFragment statisticalFragment = StatisticalFragment.newInstance();
        MyMsgFragment myMsgFragment = MyMsgFragment.newInstance();

        mTitleText = findViewById(R.id.title_text);

        lists = new ArrayList<>();
        lists.add(clockPageFragment);
        lists.add(statisticalFragment);
        lists.add(myMsgFragment);
        mViewPager = findViewById(R.id.body_show_vessel);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), lists, mTitleText));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case CLOCK_PAGE:
                        mTitleText.setText(R.string.clock_str);
                        RadioButton clock = findViewById(R.id.but_1);
                        clock.setChecked(true);
                        break;
                    case STATISTICAL_PAGE:
                        mTitleText.setText(R.string.statistics_str);
                        RadioButton sta = findViewById(R.id.but_2);
                        sta.setChecked(true);
                        break;
                    case MESSAGE_PAGE:
                        mTitleText.setText(R.string.my_str);
                        RadioButton my = findViewById(R.id.but_3);
                        my.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        RadioGroup mNavigationButton = findViewById(R.id.navigation_button);
        mNavigationButton.setOnCheckedChangeListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.but_1:
                mViewPager.setCurrentItem(CLOCK_PAGE);
                mTitleText.setText(R.string.clock_str);
                RadioButton clock = findViewById(R.id.but_1);
                clock.setChecked(true);
                break;
            case R.id.but_2:
                mViewPager.setCurrentItem(STATISTICAL_PAGE);
                mTitleText.setText(R.string.statistics_str);
                RadioButton sta = findViewById(R.id.but_2);
                sta.setChecked(true);
                break;
            case R.id.but_3:
                mViewPager.setCurrentItem(MESSAGE_PAGE);
                mTitleText.setText(R.string.my_str);
                RadioButton my = findViewById(R.id.but_3);
                my.setChecked(true);
                break;
            default:
                break;

        }
    }


    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> lists;
        private TextView mTitle;

        public ViewPagerAdapter(@NonNull FragmentManager fm, List<Fragment> lists, TextView textView) {
            super(fm);
            this.lists = lists;
            this.mTitle = textView;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return lists.get(position);
        }


    }

    public static void showToast(Context context, String note){
        Activity activity = (Activity) context;
        activity.runOnUiThread(() -> Toast.makeText(context, note, Toast.LENGTH_SHORT).show());

    }
}