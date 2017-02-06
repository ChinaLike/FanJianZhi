package com.touchrom.fanjianzhi.activity.web;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;

/**
 * Created by lyy on 2016/6/3.
 */
public class VideoWebView extends BaseWebView {

    @Override
    protected void init(Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.init(savedInstanceState);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setUserAgentString("stagefright/1.2 (Linux;Android 4.4.4)");
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.onPause();
        mWebView.destroy();
        super.onDestroy();
//        mWebView.onPause();
//        mWebView.destroy();
    }

//    @Override
//    public void finish() {
//        super.finish();
//        mWebView.onPause();
//        mWebView.destroy();
//    }
}
