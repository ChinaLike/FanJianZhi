package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by lyy on 2016/6/30.
 */
public class VideoWebView extends WebView{
    public VideoWebView(Context context) {
        super(context);
    }

    public VideoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
