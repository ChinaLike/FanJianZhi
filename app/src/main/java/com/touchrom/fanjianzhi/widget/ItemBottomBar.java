package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyy.ui.widget.IconText;
import com.touchrom.fanjianzhi.R;

/**
 * Created by lyy on 2016/6/6.
 */
public class ItemBottomBar extends LinearLayout implements View.OnClickListener {
    IconText mScan, mComment, mStar, mUnStar;
    private OnItemBottomListener mListener;
    TextView mTime;

    public interface OnItemBottomListener {
        public void onItemBottom(View view);
    }

    public ItemBottomBar(Context context) {
        this(context, null);
    }

    public ItemBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_item_bottom_bar, this, true);
        mTime = (TextView) findViewById(R.id.time);
//        mScan = (IconText) findViewById(R.id.scan);
        mComment = (IconText) findViewById(R.id.comment);
        mStar = (IconText) findViewById(R.id.star);
        mUnStar = (IconText) findViewById(R.id.un_star);
//        mScan.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mStar.setOnClickListener(this);
        mUnStar.setOnClickListener(this);
    }

    public void setTime(CharSequence text) {
        mTime.setText(text);
    }

    public void setStar(CharSequence star){
        mStar.setText(star);
    }

    public void setUnStar(CharSequence unStar){
        mUnStar.setText(unStar);
    }

    public void setComment(CharSequence comment){
        mComment.setText(comment);
    }

    public void setOnItemBottomListener(@NonNull OnItemBottomListener onIteBottmListener) {
        mListener = onIteBottmListener;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemBottom(v);
        }
    }
}
