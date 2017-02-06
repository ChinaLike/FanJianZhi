package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.LoginActivity;
import com.touchrom.fanjianzhi.activity.RegActivity;
import com.touchrom.fanjianzhi.help.ImgHelp;

/**
 * Created by lyy on 2016/6/2.
 */
public class UnLoginHeader extends LinearLayout implements View.OnClickListener {

    Button mReg;
    ImageView mImg;
    TextView mLogin;

    public UnLoginHeader(Context context) {
        this(context, null);
    }

    public UnLoginHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.main_un_login_nav_header, this, true);
        mLogin = (TextView) findViewById(R.id.login);
        mReg = (Button) findViewById(R.id.reg);
        mImg = (ImageView) findViewById(R.id.img);
        mLogin.setOnClickListener(this);
        mReg.setOnClickListener(this);
    }

    public void setImg(String imgUrl){
        ImgHelp.setImg(getContext(), imgUrl, mImg);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.login:
                intent = new Intent(getContext(), LoginActivity.class);
                break;
            case R.id.reg:
                intent = new Intent(getContext(), RegActivity.class);
                break;
        }
        if (intent != null) {
            getContext().startActivity(intent);
        }
    }
}
