package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.show.T;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivitySettingBinding;
import com.touchrom.fanjianzhi.dialog.CheckVersionDialog;
import com.touchrom.fanjianzhi.dialog.ShareDialog;
import com.touchrom.fanjianzhi.entity.SettingEntity;
import com.touchrom.fanjianzhi.entity.UpdateEntity;
import com.touchrom.fanjianzhi.module.MainModule;
import com.touchrom.fanjianzhi.module.SettingModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/16.
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding> implements View.OnClickListener {
    @InjectView(R.id.show_img)
    SwitchCompat mShowImg;
    @InjectView(R.id.clean_cache)
    RelativeLayout mCleanCache;
    @InjectView(R.id.clean_search)
    TextView mCleanSearch;
    @InjectView(R.id.check_version)
    RelativeLayout mCheckVersion;
    @InjectView(R.id.about)
    TextView mAbout;
    @InjectView(R.id.share_app)
    TextView mShareApp;
    @InjectView(R.id.login_out)
    TextView mLoginOut;
    private final int SHOW_IMG_CODE = 1, LOGIN_OUT_CODE = 2;
    CheckVersionDialog mVersionDialog;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mSetting = AppManager.getInstance().getSetting();
        getBinding().setVersionName("当前版本：" + AndroidUtils.getVersionName(this));
        getBinding().setCacheSize(FileUtil.formatFileSize(getModule(SettingModule.class).getCacheSize()));
        mCleanCache.setOnClickListener(this);
        mCleanSearch.setOnClickListener(this);
        mCheckVersion.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mShareApp.setOnClickListener(this);
        mLoginOut.setOnClickListener(this);
        mShowImg.setChecked(mSetting.isShowImg());
        mShowImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showMsgDialog("操作提示", "开启后2G/3G/4G网络将自动加载图片，但可能会产生额外费用，确定开启？", SHOW_IMG_CODE);
                } else {
                    mSetting.setShowImg(false);
                }
            }
        });
        setTitle("设置");
        mLoginOut.setVisibility(AppManager.getInstance().isLogin() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean_cache:
                showLoadingDialog();
                getModule(SettingModule.class).cleanCache();
                dismissLoadingDialog();
                getBinding().setCacheSize("0kb");
                break;
            case R.id.clean_search:
                getModule(SettingModule.class).delSearchRecord();
                T.showShort(this, "清除搜索记录成功");
                break;
            case R.id.check_version:
                mVersionDialog = new CheckVersionDialog(this);
                mVersionDialog.show(getSupportFragmentManager(), "check_version_dialog");
                getModule(MainModule.class).update(this);
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.share_app:
                ShareDialog dialog = new ShareDialog(2, 1);
                dialog.show(getSupportFragmentManager(), "share_dialog");
                break;
            case R.id.login_out:
                showMsgDialog("退出登录", "是否放弃长长的机会，成为游客贱友？", LOGIN_OUT_CODE);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.getInstance().saveSetting(mSetting);
        NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.UPDATE_SETTING);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.CHECK_VERSION) {
            final UpdateEntity entity = (UpdateEntity) obj;
            if (mVersionDialog != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVersionDialog.dismiss();
                        if (!entity.isHasNewVersion()) {
                            T.showShort(SettingActivity.this, "已经是最新版本了");
                        }
                    }
                }, 2000);
            }

        } else if (result == ResultCode.DIALOG.MSG_DIALOG_ENTER) {
            int code = (int) obj;
            if (code == SHOW_IMG_CODE) {
                mSetting.setShowImg(true);
                mShowImg.setChecked(true);
            } else if (code == LOGIN_OUT_CODE) {
                AppManager.getInstance().loginOut();
                NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, null);
                T.showShort(this, "退出登录成功");
                mLoginOut.setVisibility(View.GONE);
            }
        } else if (result == ResultCode.DIALOG.MSG_DIALOG_CANCEL) {
            int code = (int) obj;
            if (code == SHOW_IMG_CODE) {
                mSetting.setShowImg(false);
                mShowImg.setChecked(false);
            }
        }
    }
}
