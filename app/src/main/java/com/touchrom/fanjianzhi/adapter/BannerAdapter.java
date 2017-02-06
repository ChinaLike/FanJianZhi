package com.touchrom.fanjianzhi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.touchrom.fanjianzhi.entity.d_entity.BannerEntity;
import com.touchrom.fanjianzhi.fragment.BannerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2015/11/18.
 * Banner适配器
 */
public class BannerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "BannerAdapter";
    private List<BannerEntity> mEntitys = new ArrayList<>();


    public BannerAdapter(FragmentManager fm, List<BannerEntity> entities) {
        super(fm);
        mEntitys.clear();
        mEntitys.addAll(entities);
    }

    @Override
    public Fragment getItem(int position) {
        return BannerFragment.newInstance(mEntitys.get(position));
    }

    @Override
    public int getCount() {
        return mEntitys.size();
    }
}
