package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.fanjianzhi.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/21.
 */
public class ToolBar extends RelativeLayout implements View.OnClickListener {
    @InjectView(R.id.logo)
    ImageView mLogo;
    @InjectView(R.id.icon_menu)
    ImageView mMenuIcon;
    @InjectView(R.id.icon_user)
    CircleImageView mUserIcon;
    @InjectView(R.id.back)
    ImageView mBack;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_img)
    ImageView mRightBt;

    OnToolBarClickListener mListener;

    public interface OnToolBarClickListener {
        public void onBackClick(View view);

        public void onRightBtClick(View view);

        public void onMenuClick(View view);
    }

    public ToolBar(Context context) {
        this(context, null);
    }

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tool_bar, this, true);
        ButterKnife.inject(this, view);
        mBack.setOnClickListener(this);
        mRightBt.setOnClickListener(this);
        mMenuIcon.setOnClickListener(this);
        mUserIcon.setOnClickListener(this);
    }

    public ImageView getLogo() {
        return mLogo;
    }

    public ImageView getMenuIcon() {
        return mMenuIcon;
    }

    public CircleImageView getUserIcon() {
        return mUserIcon;
    }

    public ImageView getBack() {
        return mBack;
    }

    public TextView getTitle() {
        return mTitle;
    }

    public ImageView getRightBt() {
        return mRightBt;
    }

    public void setUserIcon(String icon) {
        Glide.with(getContext()).load(icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(mUserIcon);
    }

    public void setOnToolBarClickListener(OnToolBarClickListener listener) {
        mListener = listener;
    }

    public void showTitle(boolean show) {
        mTitle.setVisibility(show ? VISIBLE : GONE);
    }

    public void showLogo(boolean show) {
        mLogo.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public void setMenuIcon(@DrawableRes int drawable) {
        mRightBt.setImageResource(drawable);
    }

    public void setMenuIcon(Drawable drawable) {
        mRightBt.setImageDrawable(drawable);
    }

    public void setBackIcon(@DrawableRes int drawable) {
        mBack.setImageResource(drawable);
    }

    public void setBackIcon(Drawable drawable) {
        mBack.setImageDrawable(drawable);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.back:
                    mListener.onBackClick(v);
                    break;
                case R.id.right_img:
                    mListener.onRightBtClick(v);
                    break;
                case R.id.icon_menu:
                    mListener.onMenuClick(v);
                    break;
            }
        }
    }

}
