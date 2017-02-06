package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arialyy.frame.util.show.T;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityRegBinding;
import com.touchrom.fanjianzhi.entity.WebEntity;
import com.touchrom.fanjianzhi.help.RegexHelp;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.module.UserModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/3.
 * 注册界面
 */
public class RegActivity extends BaseActivity<ActivityRegBinding> implements View.OnClickListener {

    @InjectView(R.id.get_code)
    Button mCode;
    @InjectView(R.id.protocol)
    TextView mProtocol;
    @InjectView(R.id.phone)
    IconEditText mPhoneNum;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mCode.setOnClickListener(this);
        mProtocol.setOnClickListener(this);
        setTitle("注册新用户");
        mPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                boolean able = !TextUtils.isEmpty(str) && RegexHelp.isPhoneNumber(str);
                mPhoneNum.setLeftDrawable(able ? R.mipmap.icon_phone_able : R.mipmap.icon_phone_def);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.protocol:
                TurnHelp.getINSTANCE().turnNormalWeb(this, new WebEntity(Constance.URL.PROTOCOL));
                break;
            case R.id.get_code:
                verPhone();
                break;
        }
    }

    private void verPhone() {
        String code = mPhoneNum.getText().toString();
        if (TextUtils.isEmpty(code)) {
            T.showShort(this, "手机号不能为空");
            return;
        } else if (!RegexHelp.isPhoneNumber(code)) {
            T.showShort(this, "请输入正确的手机号");
            return;
        }
        getModule(UserModule.class).isReg(code);
    }

    private void getCode() {
        String code = mPhoneNum.getText().toString();
        if (TextUtils.isEmpty(code)) {
            T.showShort(this, "手机号不能为空");
            return;
        } else if (!RegexHelp.isPhoneNumber(code)) {
            T.showShort(this, "请输入正确的手机号");
            return;
        }
        Intent intent = new Intent(this, ConfirmPhoneActivity.class);
        intent.putExtra(Constance.KEY.STRING, mPhoneNum.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (result == ResultCode.SERVER.IS_REG) {
            if (obj != null && !(boolean) obj) {
                getCode();
            }
        }
    }

}
