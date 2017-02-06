package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.databinding.DialogLoadingBinding;

import butterknife.InjectView;


/**
 * Created by lyy on 2015/11/9.
 * 加载等待对话框
 */
@SuppressLint("ValidFragment")
public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {
//    @InjectView(R.id.img)
//    ImageView mLoading;

    public LoadingDialog(boolean canCancel) {
        setCancelable(canCancel);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
//        AnimationDrawable ad = ImgHelp.createLoadingAnim(getContext());
//        mLoading.setImageDrawable(ad);
//        ad.start();
//        Glide.with(getContext()).load(R.drawable.fjz_load_anim).into(mLoading);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_loading;
    }
}
