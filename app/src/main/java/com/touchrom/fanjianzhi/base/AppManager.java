package com.touchrom.fanjianzhi.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.arialyy.frame.util.SharePreUtil;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.entity.AccountEntity;
import com.touchrom.fanjianzhi.entity.SettingEntity;
import com.touchrom.fanjianzhi.entity.UserEntity;

/**
 * Created by lyy on 2015/11/9.
 * App管理器
 */
public class AppManager {
    private static final String TAG = "AppManager";
    private static volatile AppManager mManager = null;
    private Context mContext;
    private static final Object LOCK = new Object();
    private int mNetState = -1;

    private AppManager() {

    }

    private AppManager(Context context) {
        mContext = context;
    }

    protected static void init(Context context) {
        if (mManager == null) {
            synchronized (LOCK) {
                if (mManager == null) {
                    mManager = new AppManager(context);
                }
            }
        }
    }

    public static AppManager getInstance() {
        if (mManager == null) {
            throw new NullPointerException("请在Application里面初始化APPManager!!");
        }
        return mManager;
    }

    public void loginOut() {
        saveLoginState(false);
        SharePreUtil.removeKey(Constance.APP.NAME, mContext, Constance.KEY.USER);
        SharePreUtil.removeKey(Constance.APP.NAME, mContext, Constance.KEY.ACCOUNT);
    }

    public void saveAccount(@NonNull AccountEntity entity) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.ACCOUNT, AccountEntity.class, entity);
    }

    public AccountEntity getAccount() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.ACCOUNT, AccountEntity.class);
    }

    public void saveLoginState(boolean state) {
        SharePreUtil.putBoolean(Constance.APP.NAME, mContext, Constance.KEY.LOGIN_STATE, state);
    }

    public boolean isLogin() {
        return SharePreUtil.getBoolean(Constance.APP.NAME, mContext, Constance.KEY.LOGIN_STATE);
    }

    public UserEntity getUser() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.USER, UserEntity.class);
    }

    public void saveUser(UserEntity userEntity) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.USER, UserEntity.class, userEntity);
    }

    public SettingEntity getSetting() {
        return SharePreUtil.getObject(Constance.APP.NAME, mContext, Constance.KEY.SETTING, SettingEntity.class);
    }

    public void saveSetting(SettingEntity settingEntity) {
        SharePreUtil.putObject(Constance.APP.NAME, mContext, Constance.KEY.SETTING, SettingEntity.class, settingEntity);
    }

}
