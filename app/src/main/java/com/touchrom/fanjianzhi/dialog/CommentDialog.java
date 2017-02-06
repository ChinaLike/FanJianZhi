package com.touchrom.fanjianzhi.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.databinding.DialogCommentBinding;

/**
 * Created by lyy on 2016/6/7.
 * 发送回复对话框
 */
public class CommentDialog extends BaseDialog<DialogCommentBinding> {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(p);
        getDialog().getWindow().setWindowAnimations(R.style.dialogAnim);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_comment;
    }
}
