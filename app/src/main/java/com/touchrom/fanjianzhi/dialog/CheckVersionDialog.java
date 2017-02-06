package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;

import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.databinding.DialogCheckVersionBinding;

/**
 * Created by lyy on 2016/6/16.
 * 版本检测对话框
 */
@SuppressLint("ValidFragment")
public class CheckVersionDialog extends BaseDialog<DialogCheckVersionBinding> {

    public CheckVersionDialog(Object obj) {
        super(obj);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_check_version;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getSimplerModule().onDialog(1,1);
    }
}
