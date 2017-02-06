package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.core.NotifyHelp;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityLoginBinding;
import com.touchrom.fanjianzhi.module.UserModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/2.
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {
    @InjectView(R.id.forget_pw)
    TextView mForgetPw;
    @InjectView(R.id.reg)
    TextView mReg;
    @InjectView(R.id.account)
    IconEditText mAccount;
    @InjectView(R.id.password)
    IconEditText mPassword;
    @InjectView(R.id.login)
    Button mLogin;
    @InjectView(R.id.right_bt)
    TextView mOtherLogin;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("登录");
        mForgetPw.setOnClickListener(this);
        mReg.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mOtherLogin.setOnClickListener(this);
        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                boolean isAble = !TextUtils.isEmpty(str) && str.length() > 2 && str.length() < 12;
                mAccount.setLeftDrawable(isAble ? R.mipmap.icon_user_able : R.mipmap.icon_user_def);
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                boolean isAble = !TextUtils.isEmpty(str) && str.length() > 5 && str.length() < 40;
                mPassword.setLeftDrawable(isAble ? R.mipmap.icon_pw_able : R.mipmap.icon_pw_def);
            }
        });
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_pw:
                startActivity(new Intent(this, FindPwActivity.class));
                break;
            case R.id.reg:
                startActivity(new Intent(this, RegActivity.class));
                break;
            case R.id.login:
                login();
                break;
            case R.id.right_bt:
                startActivity(new Intent(this, LibLoginActivity.class));
                break;
        }
    }

    private void login() {
        String account = mAccount.getText().toString();
        String pw = mPassword.getText().toString();
        if (TextUtils.isEmpty(account)) {
            mAccount.setError("账号不能为空");
            return;
        } else if (TextUtils.isEmpty(pw)) {
            mPassword.setError("密码不能为空");
            return;
        }
        getModule(UserModule.class).login(account, pw);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.LOGIN) {
            NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, obj);
            finish();
        }
    }
}
