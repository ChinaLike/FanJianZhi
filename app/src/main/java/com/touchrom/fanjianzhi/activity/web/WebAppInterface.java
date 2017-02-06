package com.touchrom.fanjianzhi.activity.web;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * JS统一回调接口
 */
public class WebAppInterface {
    private Context mContext;

    public WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public int getUserId(){
        return 0;
    }
}