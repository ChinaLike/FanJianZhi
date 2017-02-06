package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.databinding.DialogVersionUpdateHintBinding;
import com.touchrom.fanjianzhi.entity.UpdateEntity;
import com.touchrom.fanjianzhi.help.RippleHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/11/12.
 * 版本升级提示对话框
 */
@SuppressLint("ValidFragment")
public class VersionUpgradeDialog extends BaseDialog<DialogVersionUpdateHintBinding> implements View.OnClickListener {

    private UpdateEntity mEntity;
    @InjectView(R.id.enter)
    Button mEnter;
    @InjectView(R.id.cancel)
    Button mCancel;

    public VersionUpgradeDialog(UpdateEntity entity) {
        mEntity = entity;
        setCancelable(!entity.isForcedUpdate());
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getBinding().setTitle(mEntity.getVersionTitle());
        getBinding().setMsg(mEntity.getVersionMsg());
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        RippleHelp.createRipple(getContext(), mEnter);
        RippleHelp.createRipple(getContext(), mCancel);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_version_update_hint;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                dismiss();
                DownloadDialog dialog = new DownloadDialog(mEntity, mObj);
                dialog.show(mActivity.getSupportFragmentManager(), "downloadDialog");
                break;
            case R.id.cancel:
                if (mEntity.isForcedUpdate()) {
                    dismiss();
                    mActivity.exitApp();
                } else {
                    dismiss();
                }
                break;
        }
    }
}
