package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.arialyy.frame.core.MVVMFrame;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.CircleIndicator;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.LauncherActivity;
import com.touchrom.fanjianzhi.activity.MainActivity;
import com.touchrom.fanjianzhi.adapter.SimpleViewPagerAdapter;
import com.touchrom.fanjianzhi.fragment.GuideFragment;

/**
 * Created by lyy on 2016/6/1.
 */
public class GuideView extends RelativeLayout {
    ViewPager mVp;
    Button mBt;
    CircleIndicator mIndicator;
    FragmentManager mFm;
    int[] mImgs = new int[]{
            R.mipmap.icon_welcome_1,
            R.mipmap.icon_welcome_2,
            R.mipmap.icon_welcome_3,
    };


    public GuideView(Context context, FragmentManager fm) {
        super(context, null);
        mFm = fm;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_guide_view, this, true);
        mVp = (ViewPager) findViewById(R.id.vp);
        mBt = (Button) findViewById(R.id.bt);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(mFm);
        for (int drawable : mImgs) {
            adapter.addFrag(GuideFragment.newInstance(drawable), "guide");
        }
        mVp.setOffscreenPageLimit(mImgs.length);
        mVp.setAdapter(adapter);
        mIndicator.setViewPager(mVp);

        mBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), MainActivity.class));
                MVVMFrame.getInstance().finishActivity(LauncherActivity.class);
            }
        });
        L.d("tag", mBt.getHeight() + "");
    }

}
