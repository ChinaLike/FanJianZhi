package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.frame.util.show.T;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.DialogImgCodeBinding;
import com.touchrom.fanjianzhi.entity.CodeResultEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.help.RippleHelp;
import com.touchrom.fanjianzhi.module.UserModule;
import com.touchrom.fanjianzhi.util.Encryption;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/8.
 * 图片验证码对话框
 */
@SuppressLint("ValidFragment")
public class ImgCodeDialog extends BaseDialog<DialogImgCodeBinding> implements View.OnClickListener {
    @InjectView(R.id.img)
    ImageView mImg;
    @InjectView(R.id.next)
    TextView mNext;
    @InjectView(R.id.img_code_et)
    EditText mImgCode;
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.enter)
    Button mEnter;

    private String mImgUrl, mPhone;

    public ImgCodeDialog(Object object, String phone, String imgUrl) {
        super(object);
        this.mImgUrl = imgUrl;
        mPhone = phone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mNext.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mEnter.setOnClickListener(this);
        RippleHelp.createRipple(getContext(), mCancel);
        RippleHelp.createRipple(getContext(), mEnter);
        ImgHelp.setImg(getContext(), mImgUrl + "?hehe=" + Encryption.getRandomCode(), mImg);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_img_code;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                getSimplerModule().onDialog(ResultCode.DIALOG.IMG_CODE_DIALOG_CANCEL, false);
                dismiss();
                return;
            case R.id.enter:
                String imgCode = mImgCode.getText().toString();
                if (!TextUtils.isEmpty(imgCode)) {
//                    getSimplerModule().onDialog(ResultCode.DIALOG.IMG_CODE_DIALOG_ENTER, imgCode);
                    getModule(UserModule.class).confirmImgCode(mPhone, imgCode);
                } else {
                    mImgCode.setError("验证码不能为空");
                }
                break;
            case R.id.next:
                ImgHelp.setImg(getContext(), mImgUrl + "?hehe=" + Encryption.getRandomCode(), mImg);
                break;
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.CONFIRM_IMG_CODE) {  //验证图片验证码
            CodeResultEntity codeREntity = (CodeResultEntity) obj;
            T.showShort(getContext(), codeREntity.isSuccess() ? "图片验证成功" : "图片验证失败");
            if (codeREntity.isSuccess()) {
                getSimplerModule().onDialog(ResultCode.DIALOG.IMG_CODE_DIALOG_ENTER, true);
                dismiss();
            } else {
                mImgCode.setText("");
                ImgHelp.setImg(getContext(), mImgUrl + "?hehe=" + Encryption.getRandomCode(), mImg);
            }
        }
    }
}
