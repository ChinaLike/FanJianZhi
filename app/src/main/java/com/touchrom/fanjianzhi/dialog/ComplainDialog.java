package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arialyy.frame.util.show.L;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.databinding.DialogComplainBinding;
import com.touchrom.fanjianzhi.module.ArtModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/7.
 * 投诉对话框
 */
@SuppressLint("ValidFragment")
public class ComplainDialog extends BaseDialog<DialogComplainBinding> implements View.OnClickListener {
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.enter)
    Button mEnter;
    @InjectView(R.id.rg)
    RadioGroup mRg;
    private int mComplainId = -1;
    private int mArtId;

    public ComplainDialog(int artId) {
        mArtId = artId;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mEnter.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        for (int i = 0, count = mRg.getChildCount(); i < count; i++) {
            RadioButton rb = (RadioButton) mRg.getChildAt(i);
            rb.setId(i);
        }
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.getChildAt(checkedId);
                if (rb != null) {
                    mComplainId = Integer.valueOf(rb.getTag() + "");
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_complain;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enter) {
            if (mComplainId == -1) {
                return;
            }
            getModule(ArtModule.class).complaints(mArtId, mComplainId);
        }
        dismiss();
    }
}
