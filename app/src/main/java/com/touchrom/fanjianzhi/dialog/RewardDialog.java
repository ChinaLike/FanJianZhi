package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.T;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.databinding.DialogComplainBinding;
import com.touchrom.fanjianzhi.module.ArtModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/7.
 * 打赏对话框
 */
@SuppressLint("ValidFragment")
public class RewardDialog extends BaseDialog<DialogComplainBinding> implements View.OnClickListener {
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.enter)
    Button mEnter;
    @InjectView(R.id.rg)
    RadioGroup mRg;
    @InjectView(R.id.info)
    TextView mInfo;
    @InjectView(R.id.money)
    TextView mMoney;
    private int mComplainId = -1;
    String mNikeName;
    int mAuthorId, mArtId;

    public RewardDialog(int authorId, int artId, String nikeName){
        mNikeName = nikeName;
        mAuthorId = authorId;
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
                if (rb != null && rb.isChecked()) {
                    mComplainId = Integer.valueOf(rb.getTag() + "");
                }
            }
        });
        String str = "赏给" + mNikeName +"多上贱币？";
        mInfo.setText(StringUtil.highLightStr(str, mNikeName, getContext().getResources().getColor(R.color.colorPrimary)));
        String money = AppManager.getInstance().getUser().getMoney() + "";
        str = mMoney.getText().toString() + money;
        mMoney.setText(StringUtil.highLightStr(str, money, Color.parseColor("#ff6600")));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_reward;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enter) {
            if (mComplainId == -1) {
                return;
            }else if (AppManager.getInstance().getUser().getMoney() == 0){
                T.showShort(getContext(), "余额不足");
                return;
            }
            getModule(ArtModule.class).reward(mAuthorId, mArtId, mComplainId, 1);
        }
        dismiss();
    }
}
