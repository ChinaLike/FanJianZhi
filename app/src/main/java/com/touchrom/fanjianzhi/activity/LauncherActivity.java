package com.touchrom.fanjianzhi.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arialyy.frame.permission.OnPermissionCallback;
import com.arialyy.frame.permission.PermissionManager;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.show.T;
import com.lyy.ui.widget.JumpBt;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityLauncherBinding;
import com.touchrom.fanjianzhi.module.LauncherModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/1.
 */
public class LauncherActivity extends BaseActivity<ActivityLauncherBinding> {
    @InjectView(R.id.root)
    RelativeLayout mRootView;
    @InjectView(R.id.jump_bt)
    JumpBt mBt;
    @InjectView(R.id.img)
    ImageView mImg;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_launcher;
    }

    public RelativeLayout getRootView() {
        return mRootView;
    }

    public void startBt() {
        mBt.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBt.start();
            }
        }, 1000);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (AndroidVersionUtil.hasM() && !checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionManager.getInstance().requestPermission(this, new OnPermissionCallback() {
                @Override
                public void onSuccess(String... permissions) {
                    getModule(LauncherModule.class).startLauncherFlows(LauncherActivity.this, mImg);
                }

                @Override
                public void onFail(String... permissions) {
                    T.showShort(LauncherActivity.this, "犯贱志需要你开启文件权限用来数据缓存，以提升你的使用体验");
                }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        } else {
            getModule(LauncherModule.class).startLauncherFlows(this, mImg);
        }
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBt.stop();
                getModule(LauncherModule.class).startApp();
            }
        });

        mBt.setOnFinishListener(new JumpBt.OnFinishListener() {
            @Override
            public void onFinish() {
                getModule(LauncherModule.class).startApp();
            }
        });
    }

    public boolean checkPermission(Activity activity, String permission) {
        return AndroidVersionUtil.hasM() && activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.ACTIVITY.FIRST_LAUNCHER) {
            mBt.setVisibility(View.GONE);
        }
    }
}
