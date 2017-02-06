package com.touchrom.fanjianzhi.base;

import android.app.Application;
import android.content.Context;

import com.arialyy.frame.core.MVVMFrame;
import com.arialyy.frame.util.CalendarUtils;
import com.touchrom.fanjianzhi.entity.AccountEntity;
import com.touchrom.fanjianzhi.entity.SettingEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.litepal.LitePalApplication;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lyy on 2015/11/6.
 */
public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
//        MVVMFrame.init(this).openCrashHandler();
        MVVMFrame.init(this);
        mContext = getApplicationContext();
        ServiceUtil.getInstance(this);
        AppManager.init(getApplicationContext());
        setDefaultInfo();
        ShareSDK.initSDK(this);
    }

    /**
     * 设置默认信息
     */
    private void setDefaultInfo() {
        AppManager app = AppManager.getInstance();
        SettingEntity settingEntity = app.getSetting();
        if (settingEntity == null) {
            settingEntity = new SettingEntity();
            settingEntity.setLastCheckTime(CalendarUtils.formatDateTimeWithTime(System.currentTimeMillis() + ""));
            app.saveSetting(settingEntity);
        }
        AccountEntity account = app.getAccount();
        if (account == null) {
            account = new AccountEntity(false);
            app.saveAccount(account);
        }
    }

    public static Context getAppContext() {
        return mContext;
    }
}
