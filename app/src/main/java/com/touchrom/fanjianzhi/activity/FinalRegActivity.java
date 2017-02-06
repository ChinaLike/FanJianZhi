package com.touchrom.fanjianzhi.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.core.MVVMFrame;
import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityFinalRegBinding;
import com.touchrom.fanjianzhi.entity.UserEntity;
import com.touchrom.fanjianzhi.module.UserModule;

import java.util.Stack;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/8.
 * 最终注册
 */
public class FinalRegActivity extends BaseActivity<ActivityFinalRegBinding> {
    @InjectView(R.id.hint)
    TextView mHint;
    @InjectView(R.id.nike_name)
    IconEditText mNikeName;
    @InjectView(R.id.password)
    IconEditText mPassword;
    @InjectView(R.id.final_reg)
    Button mReg;
    String mPhone;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mPhone = getIntent().getStringExtra(Constance.KEY.STRING);
        if (TextUtils.isEmpty(mPhone)) {
            L.e(TAG, "手机号不能为空");
            finish();
            return;
        }
        setTitle("注册信息");
        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nikeName = mNikeName.getText().toString();
                String password = mPassword.getText().toString();
                if (TextUtils.isEmpty(nikeName)) {
                    mHint.setText(setErrorInfo("昵称不能为空"));
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    mHint.setText(setErrorInfo("密码不能为空"));
                    return;
                }
                getModule(UserModule.class).reg(mPhone, nikeName, password);
            }
        });
        mNikeName.addTextChangedListener(new TextWatcher() {
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
                mNikeName.setLeftDrawable(isAble ? R.mipmap.icon_nike_name_able : R.mipmap.icon_nike_name_def);
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

    private CharSequence setErrorInfo(String errorHint) {
        String hint = "完善注册信息 ";
        return StringUtil.highLightStr(hint + errorHint, errorHint, getResources().getColor(R.color.red));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_final_reg;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.REG) {
            if (obj instanceof UserEntity) {
                NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, obj);
                finish();
                MVVMFrame.getInstance().finishActivity(ConfirmPhoneActivity.class);
                MVVMFrame.getInstance().finishActivity(RegActivity.class);
                Stack<AbsActivity> activities = MVVMFrame.getInstance().getActivityStack();
                for (AbsActivity absActivity : activities) {
                    if (absActivity instanceof LoginActivity) {
                        absActivity.finish();
                    }
                }
            } else {
                mHint.setText(setErrorInfo(obj + ""));
            }
        }
    }
}
