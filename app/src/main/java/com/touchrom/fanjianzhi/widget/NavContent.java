package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyy.ui.widget.RedDotLayout;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.help.RippleHelp;

/**
 * Created by lyy on 2016/6/2.
 */
public class NavContent extends LinearLayout implements View.OnClickListener {

    private OnItemListener mListener;
    private RelativeLayout mMessage, mCollect, mSetting, mSigned, mSignBg;
    private RedDotLayout mRedDot;
    private TextView mSign, mSignNum;

    public interface OnItemListener {
        public void onItem(View item);
    }

    public NavContent(Context context) {
        this(context, null);
    }

    public NavContent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.main_item_content, this, true);
        mMessage = (RelativeLayout) findViewById(R.id.message);
        mCollect = (RelativeLayout) findViewById(R.id.collect);
        mSetting = (RelativeLayout) findViewById(R.id.setting);
        mRedDot = (RedDotLayout) findViewById(R.id.redDot);
        mSigned = (RelativeLayout) findViewById(R.id.signed);
        mSignBg = (RelativeLayout) findViewById(R.id.sign_bg);
        mSign = (TextView) findViewById(R.id.sign);
        mSignNum = (TextView) findViewById(R.id.sign_num);
        RippleHelp.createRipple(getContext(), mMessage);
        RippleHelp.createRipple(getContext(), mCollect);
        RippleHelp.createRipple(getContext(), mSetting);
        mMessage.setOnClickListener(this);
        mCollect.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSignBg.setOnClickListener(this);
    }

    /**
     * 是否已签到
     *
     * @param isSigned true：已签到，false：未签到
     */
    public void setIsSign(boolean isSigned) {
        if (isSigned) {
            mSignBg.setEnabled(false);
            mSign.setVisibility(GONE);
            mSigned.setVisibility(VISIBLE);
        } else {
            mSignBg.setEnabled(true);
            mSign.setVisibility(VISIBLE);
            mSigned.setVisibility(GONE);
        }
    }

    /**
     * 显示签到按钮
     */
    private void showSignBt(boolean show) {
        mSignBg.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 设置签到天数
     */
    public void setSignDayNum(int num) {
        mSignNum.setText("已连续签到" + num + "天");
    }

    /**
     * 设置消息、收藏是否能点击
     *
     * @param able
     */
    public void setFunctionAble(boolean able) {
        showSignBt(able);
        mMessage.setEnabled(able);
        mCollect.setEnabled(able);
        if (!able) {
            mMessage.setBackgroundColor(getResources().getColor(R.color.item_un_click));
            mCollect.setBackgroundColor(getResources().getColor(R.color.item_un_click));
        } else {
            mMessage.setBackgroundColor(getResources().getColor(R.color.transparent));
            mCollect.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    public void setOnItemListener(@NonNull OnItemListener listener) {
        mListener = listener;
    }

    /**
     * 设置消息数
     *
     * @param num
     */
    public void setMsgNum(int num) {
        if (num != 0) {
            mRedDot.setVisibility(VISIBLE);
            mRedDot.setText(num + "");
        } else {
            mRedDot.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItem(v);
        }
    }
}
