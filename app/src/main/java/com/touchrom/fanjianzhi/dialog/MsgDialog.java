package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.DialogMsgBinding;
import com.touchrom.fanjianzhi.help.RippleHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/11/9.
 * 消息对话框
 */
@SuppressLint("ValidFragment")
public class MsgDialog extends BaseDialog<DialogMsgBinding> implements View.OnClickListener {
    private String mTitle, mMsg;
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.enter)
    Button mEnter;
    private int mRequestCode = -1;

    /**
     * @param title
     * @param msg
     * @param obj
     * @param requestCode 请求码
     */
    public MsgDialog(String title, String msg, Object obj, int requestCode) {
        super(obj);
        mTitle = title;
        mMsg = msg;
        mRequestCode = requestCode;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getBinding().setTitle(TextUtils.isEmpty(mTitle) ? "提示" : mTitle);
        getBinding().setMsg(mMsg);
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        RippleHelp.createRipple(getContext(), mEnter);
        RippleHelp.createRipple(getContext(), mCancel);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_msg;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enter) {
//            getSimplerModule().onDialog(ResultCode.DIALOG.MSG_DIALOG_ENTER, mRequestCode);
            getSimplerModule().onDialog(ResultCode.DIALOG.MSG_DIALOG_ENTER, mRequestCode);
        } else {
            getSimplerModule().onDialog(ResultCode.DIALOG.MSG_DIALOG_CANCEL, mRequestCode);
        }
        dismiss();
    }
}
