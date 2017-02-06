package com.touchrom.fanjianzhi.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.arialyy.frame.util.show.L;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.SimpleViewPagerAdapter;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.base.BaseFragment;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.databinding.ActivityChannelBinding;
import com.touchrom.fanjianzhi.entity.TabEntity;
import com.touchrom.fanjianzhi.fragment.ChannelArtListFragment;
import com.touchrom.fanjianzhi.pop.SearchPop;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 */
public class ChannelActivity extends BaseActivity<ActivityChannelBinding> {
    @InjectView(R.id.vp)
    ViewPager mVp;
    @InjectView(R.id.tab)
    TabLayout mTab;

    private int mChannelId = -1;
    List<BaseFragment> mFragments = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mChannelId = getIntent().getIntExtra(Constance.KEY.ID, -1);
        if (mChannelId == -1) {
            L.d(TAG, "频道Id错误");
            finish();
            return;
        }
        setTitle(getIntent().getStringExtra(Constance.KEY.STRING));
        List<TabEntity> tabs = new ArrayList<>();
        tabs.add(new TabEntity(1, "推荐"));
        tabs.add(new TabEntity(2, "最热"));
        tabs.add(new TabEntity(3, "评价最多"));
        setupContentViewPager(tabs);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(List<TabEntity> tabs) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        int i = 0;
        for (TabEntity tab : tabs) {
            mFragments.add(ChannelArtListFragment.newInstance(mChannelId, tab, i == 0));
            adapter.addFrag(mFragments.get(i), tab.getTitle());
            i++;
        }
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(tabs.size());
        mTab.setTabMode(tabs.size() <= 5 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        mTab.setupWithViewPager(mVp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            SearchPop pop = new SearchPop(this, true);
            pop.showAtLocation(getRootView(), Gravity.NO_GRAVITY, 0, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_channel;
    }
}
