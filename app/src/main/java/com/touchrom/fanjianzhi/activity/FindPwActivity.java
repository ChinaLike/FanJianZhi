package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.arialyy.frame.util.show.T;
import com.lyy.ui.widget.IconEditText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityFindPwBinding;
import com.touchrom.fanjianzhi.dialog.ImgCodeDialog;
import com.touchrom.fanjianzhi.entity.CodeResultEntity;
import com.touchrom.fanjianzhi.help.RegexHelp;
import com.touchrom.fanjianzhi.module.UserModule;
import com.touchrom.fanjianzhi.util.S;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/12.
 * 找回密码
 */
public class FindPwActivity extends BaseActivity<ActivityFindPwBinding> implements View.OnClickListener {
    @InjectView(R.id.phone)
    IconEditText mPhone;
    @InjectView(R.id.code)
    IconEditText mCode;
    @InjectView(R.id.next)
    Button mNext;
    @InjectView(R.id.get_code)
    Button mGetCode;
    private CodeResultEntity mCodeREntity;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isPhone = RegexHelp.isPhoneNumber(s.toString());
                mGetCode.setEnabled(isPhone);
                mPhone.setLeftDrawable(isPhone ? R.mipmap.icon_phone_able : R.mipmap.icon_phone_def);
            }
        });
        mCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCode.setLeftDrawable(TextUtils.isEmpty(s.toString()) ? R.mipmap.icon_code_def : R.mipmap.icon_code_able);
            }
        });
        mGetCode.setOnClickListener(this);
        mNext.setOnClickListener(this);
        setTitle("找回密码");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_find_pw;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                getModule(UserModule.class).getCode(mPhone.getText().toString(), 2);
                break;
            case R.id.next:
                if (TextUtils.isEmpty(mCode.getText())) {
                    T.showShort(this, "验证码不能为空");
                    return;
                }
                if (mCodeREntity == null || !mCodeREntity.isShowImgCode()) {
                    getModule(UserModule.class).confirmMsgCode(mPhone.getText().toString(), mCode.getText().toString());
                } else {
                    showImgCodeDialog(mCodeREntity.getImgUrl());
                }
                break;
        }
    }

    private void showImgCodeDialog(String imgCodeUrl) {
        ImgCodeDialog dialog = new ImgCodeDialog(this, mPhone.getText().toString(), imgCodeUrl);
        dialog.show(getSupportFragmentManager(), "img_code_dialog");
    }

    private void next() {
        String phone = mPhone.getText().toString();
        String code = mCode.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            S.i(mRootView, "手机号不能为空");
            return;
        } else if (TextUtils.isEmpty(code)) {
            S.i(mRootView, "验证码不能为空");
            return;
        }
        Intent intent = new Intent(this, ModifyActivity.class);
        intent.putExtra(Constance.KEY.STRING, phone);
        startActivity(intent);
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.CONFIRM_MSG_CODE) {     //验证短信验证码
            mCodeREntity = (CodeResultEntity) obj;
//            mNext.setEnabled(mCodeREntity.isSuccess());
            T.showShort(this, "验证码验证" + (mCodeREntity.isSuccess() ? "成功" : "失败，请重新输入验证码"));
            if (!mCodeREntity.isSuccess()) {
                mCode.setText("");
            } else {
                next();
            }
        } else if (result == ResultCode.DIALOG.IMG_CODE_DIALOG_ENTER || result == ResultCode.DIALOG.IMG_CODE_DIALOG_CANCEL) {
            mCodeREntity.setShowImgCode(!(Boolean) obj);
        }
    }
}
