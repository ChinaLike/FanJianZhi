package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.arialyy.frame.util.KeyBoardUtils;
import com.arialyy.frame.util.show.T;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.DialogReplayBinding;
import com.touchrom.fanjianzhi.module.ArtModule;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/16.
 * 回复对话框
 */
@SuppressLint("ValidFragment")
public class ReplayDialog extends BaseDialog<DialogReplayBinding> {
    @InjectView(R.id.comment_et)
    EditText mReplayEt;
    @InjectView(R.id.commit)
    Button mCommit;

    private int mCommitId;
    private String mNikeName;

    public ReplayDialog(int commitId, String nikeName) {
        mCommitId = commitId;
        mNikeName = nikeName;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(p);
        getDialog().getWindow().setWindowAnimations(R.style.dialogAnim);

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mReplayEt.getText())) {
                    return;
                }
                getModule(ArtModule.class).comment(2, mCommitId, mReplayEt.getText().toString());
            }
        });

        mReplayEt.setHint("回复 " + mNikeName);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_replay;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.ART_COMMENT) {
            mReplayEt.setText("");
            T.showShort(getContext(), "回复成功");
            KeyBoardUtils.closeKeybord(mReplayEt, getContext());
            T.showShort(getContext(), "评论成功");
            dismiss();
        }
    }
}
