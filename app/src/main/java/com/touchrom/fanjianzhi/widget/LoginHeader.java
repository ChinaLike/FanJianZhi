package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.LoginActivity;
import com.touchrom.fanjianzhi.activity.RegActivity;
import com.touchrom.fanjianzhi.help.ImgHelp;

/**
 * Created by lyy on 2016/6/2.
 */
public class LoginHeader extends RelativeLayout implements View.OnClickListener {

    ImageView mImg;
    TextView mNikeName, mLeave, mInfo;

    public LoginHeader(Context context) {
        this(context, null);
    }

    public LoginHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.main_login_nav_header, this, true);
        mNikeName = (TextView) findViewById(R.id.nike_name);
        mImg = (ImageView) findViewById(R.id.img);
        mLeave = (TextView) findViewById(R.id.leave);
        mInfo = (TextView) findViewById(R.id.info);
        mImg.setOnClickListener(this);
    }

    public void setImg(String imgUrl) {
        ImgHelp.setImg(getContext(), imgUrl, mImg);
    }

    public void setNikeName(CharSequence nikeName) {
        mNikeName.setText(nikeName);
    }

    public void setInfo(CharSequence info) {
        mInfo.setText(info);
    }

    public void setLeave(CharSequence leave) {
        mLeave.setText(leave);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.img:
                break;
        }
        if (intent != null) {
            getContext().startActivity(intent);
        }
    }
}
