package com.touchrom.fanjianzhi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.arialyy.frame.core.MVVMFrame;
import com.arialyy.frame.core.NotifyHelp;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityLibLoginBinding;
import com.touchrom.fanjianzhi.module.UserModule;

import butterknife.InjectView;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by lyy on 2016/6/8.
 * 第三方登录
 */
public class LibLoginActivity extends BaseActivity<ActivityLibLoginBinding> implements View.OnClickListener {
    @InjectView(R.id.right_bt)
    TextView mAccountLogin;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("第三方登录");
        mAccountLogin.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_lib_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_bt:
                finish();
                break;
            case R.id.weibo_login:
                getModule(UserModule.class).libLogin(3, SinaWeibo.NAME);
                break;
            case R.id.qq_login:
                getModule(UserModule.class).libLogin(1, QQ.NAME);
                break;
            case R.id.wx_login:
                getModule(UserModule.class).libLogin(2, Wechat.NAME);
                break;
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.LIB_LOGIN) {
            NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, obj);
            finish();
            MVVMFrame.getInstance().finishActivity(LoginActivity.class);
        }
    }
}
