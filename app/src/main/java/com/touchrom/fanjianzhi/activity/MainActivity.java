package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.arialyy.frame.core.NotifyHelp;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.SimpleViewPagerAdapter;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityMainBinding;
import com.touchrom.fanjianzhi.entity.AccountEntity;
import com.touchrom.fanjianzhi.entity.TabEntity;
import com.touchrom.fanjianzhi.entity.UserEntity;
import com.touchrom.fanjianzhi.entity.WebEntity;
import com.touchrom.fanjianzhi.fragment.MainArtListFragment;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.module.MainModule;
import com.touchrom.fanjianzhi.module.UserModule;
import com.touchrom.fanjianzhi.pop.SearchPop;
import com.touchrom.fanjianzhi.widget.LoginHeader;
import com.touchrom.fanjianzhi.widget.NavContent;
import com.touchrom.fanjianzhi.widget.UnLoginHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/1.
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavContent.OnItemListener, NotifyHelp.OnNotifyCallback {
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDLayout;
    @InjectView(R.id.nav_view)
    NavigationView mNv;
    @InjectView(R.id.vp)
    ViewPager mVp;
    @InjectView(R.id.tab)
    TabLayout mTab;
    @InjectView(R.id.fb)
    FloatingActionButton mFb;
    NavContent nContent;
    List<MainArtListFragment> mFragments = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mBar.showTitle(false);
        mBar.getBack().setVisibility(View.GONE);
        mBar.getMenuIcon().setVisibility(View.VISIBLE);
        mBar.getUserIcon().setVisibility(View.VISIBLE);
        mBar.getRightBt().setVisibility(View.VISIBLE);
        mBar.getLogo().setVisibility(View.VISIBLE);
        mBar.getRightBt().setImageResource(R.mipmap.icon_search);
        nContent = new NavContent(this);
        nContent.setOnItemListener(this);
        mFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebEntity entity = new WebEntity();
                entity.setTitle("意见反馈");
                entity.setContentUrl(Constance.URL.FEEDBACK);
                TurnHelp.getINSTANCE().turnNormalWeb(MainActivity.this, entity);
            }
        });
        mBar.getUserIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDLayout.openDrawer(Gravity.LEFT);
            }
        });
        if (AppManager.getInstance().isLogin()) {
            AccountEntity account = AppManager.getInstance().getAccount();
            if (account.isLibAccount()) {
                AccountEntity.LibAccount libAccount = account.getLibAccount();
                if (!TextUtils.isEmpty(libAccount.getLibLoginName())) {
                    getModule(UserModule.class).libLogin(libAccount.getType(), libAccount.getLibLoginName());
                }
            } else {
                if (!TextUtils.isEmpty(account.getAccount()) && !TextUtils.isEmpty(account.getPassword())) {
                    getModule(UserModule.class).login(account.getAccount(), account.getPassword());
                }
            }
        }
        initHeader(AppManager.getInstance().getUser());
        getModule(MainModule.class).update(this);
        getModule(MainModule.class).getTab();
        NotifyHelp.getInstance().addObj(Constance.NOTIFY_FLAG.LOGIN, this);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager(List<TabEntity> tabs) {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        int i = 0;
//        mFragments.add(MainArtListFragment.newInstance(tabs.get(0), i != 0));
//        adapter.addFrag(mFragments.get(i), tabs.get(0).getTitle());
        for (TabEntity tab : tabs) {
            mFragments.add(MainArtListFragment.newInstance(tab, i == 0));
            adapter.addFrag(mFragments.get(i), tab.getTitle());
            i++;
        }
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(tabs.size());
        mTab.setTabMode(tabs.size() <= 5 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        mTab.setupWithViewPager(mVp);
    }

    private void initHeader(UserEntity entity) {
        View headerView = mNv.getHeaderView(0);
        if (!AppManager.getInstance().isLogin()) {
            mNv.removeHeaderView(headerView);
            UnLoginHeader unLoginHeader = new UnLoginHeader(this);
            mNv.addHeaderView(unLoginHeader);
        } else {
            LoginHeader loginHeader;
            if (headerView instanceof LoginHeader) {
                loginHeader = (LoginHeader) headerView;
            } else {
                mNv.removeHeaderView(headerView);
                loginHeader = new LoginHeader(this);
                mNv.addHeaderView(loginHeader);
            }
            mBar.setUserIcon(entity.getImgUrl());
            loginHeader.setImg(entity.getImgUrl());
            loginHeader.setNikeName(entity.getNikeName());
            loginHeader.setInfo("经验：" + entity.getExp() + "  贱币：" + entity.getMoney() + "  真贱币：" + entity.getRealMoney());
            loginHeader.setLeave(entity.getLevel());
        }
        nContent.setFunctionAble(AppManager.getInstance().isLogin());

        if (AppManager.getInstance().isLogin()) {
            nContent.setIsSign(entity.isSigned());
            nContent.setSignDayNum(entity.getSignDay());
        }
        View content = mNv.getHeaderView(1);
        if (content == null) {
            mNv.addHeaderView(nContent);
        } else {
            mNv.removeHeaderView(nContent);
            mNv.addHeaderView(nContent);
        }
    }

    @Override
    public void onMenuClick(View view) {
        super.onMenuClick(view);
        mDLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onRightBtClick(View view) {
        super.onRightBtClick(view);
        SearchPop pop = new SearchPop(this, true);
        pop.showAtLocation(getRootView(), Gravity.NO_GRAVITY, 0, 0);
    }

    @Override
    public void onBackPressed() {
        if (mDLayout.isDrawerOpen(GravityCompat.START)) {
            mDLayout.closeDrawer(GravityCompat.START);
        } else {
            if (onDoubleClickExit()) {
                exitApp();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItem(View item) {
        switch (item.getId()) {
            case R.id.message:
                startActivity(new Intent(this, MsgCenterActivity.class));
                break;
            case R.id.collect:
                Intent intent = new Intent(this, MsgActivity.class);
                intent.putExtra(Constance.KEY.TYPE, Constance.ADAPTER.MSG_COLLECT);
                startActivity(intent);
                break;
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.sign_bg:
                getModule(UserModule.class).sign();
                break;
        }
        mDLayout.closeDrawers();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_TAB_DATA) {
            setupContentViewPager((List<TabEntity>) obj);
        } else if (result == ResultCode.SERVER.LOGIN) {
            initHeader((UserEntity) obj);
        }
    }

    @Override
    public void onNotify(int action, Object obj) {
        if (action == Constance.NOTIFY_ACTION.LOGIN_ACTION) {
            initHeader((UserEntity) obj);
        } else if (action == Constance.NOTIFY_ACTION.UPDATE_SETTING) {
            for (MainArtListFragment fragment : mFragments) {
                fragment.updateSetting();
            }
        }
    }
}
