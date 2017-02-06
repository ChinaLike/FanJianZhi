package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.L;
import com.arialyy.frame.util.show.T;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityConfirmPhoneBinding;
import com.touchrom.fanjianzhi.dialog.ImgCodeDialog;
import com.touchrom.fanjianzhi.entity.CodeResultEntity;
import com.touchrom.fanjianzhi.module.UserModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/8.
 * 确认手机号
 */
public class ConfirmPhoneActivity extends BaseActivity<ActivityConfirmPhoneBinding> implements View.OnClickListener {
    @InjectView(R.id.hint)
    TextView mHint;
    @InjectView(R.id.phone_num)
    TextView mPhoneNum;
    @InjectView(R.id.next)
    TextView mNext;
    @InjectView(R.id.re_send_code)
    TextView mReSendCode;
    @InjectView(R.id.et_code)
    EditText mEt;
    private String mPhone;
    private CodeResultEntity mCodeREntity;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("验证手机号");
        mPhone = getIntent().getStringExtra(Constance.KEY.STRING);
        if (TextUtils.isEmpty(mPhone)) {
            L.e(TAG, "手机号不能为空");
            finish();
            return;
        }
        mPhoneNum.setText("+86 " + mPhone);
        mNext.setOnClickListener(this);
        mReSendCode.setOnClickListener(this);
        getModule(UserModule.class).getCode(mPhone, 1);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_confirm_phone;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (TextUtils.isEmpty(mEt.getText())) {
                    T.showShort(this, "验证码不能为空");
                    return;
                }
                if (mCodeREntity == null || !mCodeREntity.isShowImgCode()) {
                    getModule(UserModule.class).confirmMsgCode(mPhone, mEt.getText().toString());
                } else {
                    showImgCodeDialog(mCodeREntity.getImgUrl());
                }
                break;
            case R.id.re_send_code:
                getModule(UserModule.class).getCode(mPhone, 1);
                break;
        }
    }

    private void showImgCodeDialog(String imgCodeUrl) {
        ImgCodeDialog dialog = new ImgCodeDialog(this, mPhone, imgCodeUrl);
        dialog.show(getSupportFragmentManager(), "img_code_dialog");
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.CONFIRM_MSG_CODE) {     //验证短信验证码
            mCodeREntity = (CodeResultEntity) obj;
//            mNext.setEnabled(mCodeREntity.isSuccess());
            T.showShort(this, "验证码验证" + (mCodeREntity.isSuccess() ? "成功" : "失败"));
            if (!mCodeREntity.isSuccess()) {
                String str = "验证码已发送到以下手机，请在5分钟内注册 验证码错误";
                mEt.setText("");
                mHint.setText(StringUtil.highLightStr(str, " 验证码错误", getResources().getColor(R.color.red)));
            } else {
                Intent intent = new Intent(this, FinalRegActivity.class);
                intent.putExtra(Constance.KEY.STRING, mPhone);
                startActivity(intent);
            }
        } else if (result == ResultCode.DIALOG.IMG_CODE_DIALOG_ENTER || result == ResultCode.DIALOG.IMG_CODE_DIALOG_CANCEL) {
            mCodeREntity.setShowImgCode(!(Boolean) obj);
        }
    }
}
