package com.touchrom.fanjianzhi.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.core.MVVMFrame;
import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityModifyPwBinding;
import com.touchrom.fanjianzhi.entity.UserEntity;
import com.touchrom.fanjianzhi.module.UserModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/12.
 * 修改密码
 */
public class ModifyActivity extends BaseActivity<ActivityModifyPwBinding> {
    @InjectView(R.id.modify_et)
    IconEditText mPw;
    @InjectView(R.id.modify)
    Button mbt;
    @InjectView(R.id.hint)
    TextView mHint;
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
        setTitle("修改密码");
        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = mPw.getText().toString();
                if (TextUtils.isEmpty(pw)) {
                    setErrorInfo("密码不能为空");
                    return;
                }
                getModule(UserModule.class).modifyPw(mPhone, pw);
            }
        });
    }

    private void setErrorInfo(String errorHint) {
        String hint = "完善注册信息 ";
        mHint.setText(StringUtil.highLightStr(hint + errorHint, errorHint, getResources().getColor(R.color.red)));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_modify_pw;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.MODIFY_PW) {
            if (obj instanceof UserEntity) {
                NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, obj);
                finish();
                MVVMFrame.getInstance().finishActivity(FindPwActivity.class);
                MVVMFrame.getInstance().finishActivity(LoginActivity.class);
            } else {
                setErrorInfo(obj + "");
            }
        }
    }

}
