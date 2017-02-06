package com.touchrom.fanjianzhi.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseFragment;
import com.touchrom.fanjianzhi.databinding.TestFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/2.
 */
public class TestFragment extends BaseFragment<TestFragmentBinding> {
    @InjectView(R.id.list)
    RecyclerView mList;
    @InjectView(R.id.sf)
    SwipeRefreshLayout mSf;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        test();
        mSf.setColorScheme(new int[]{android.R.color.holo_red_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light});
        mSf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSf.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    public void refresh(){
        mSf.setRefreshing(true);
    }

    private void test() {
        TestAdapter adapter = new TestAdapter(getContext(), getData());
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setAdapter(adapter);
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("item ==> " + i);
        }
        return data;
    }
}
