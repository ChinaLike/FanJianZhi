package com.touchrom.fanjianzhi.base;

import android.annotation.SuppressLint;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.arialyy.frame.core.AbsDialogFragment;
import com.bumptech.glide.Glide;

/**
 * Created by lyy on 2015/11/9.
 */
@SuppressLint("ValidFragment")
public abstract class BaseDialog<VB extends ViewDataBinding> extends AbsDialogFragment<VB> {
    public BaseDialog() {
    }

    public BaseDialog(Object object) {
        super(object);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void dataCallback(int result, Object obj) {

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getContext()).clearMemory();
    }
}
