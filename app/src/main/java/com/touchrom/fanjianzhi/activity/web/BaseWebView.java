package com.touchrom.fanjianzhi.activity.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.L;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.databinding.ActivityBaseWebBinding;
import com.touchrom.fanjianzhi.entity.WebEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2015/12/3.
 * webView基类
 */
public class BaseWebView extends BaseActivity<ActivityBaseWebBinding> {
    @InjectView(R.id.webView)
    com.touchrom.fanjianzhi.widget.VideoWebView mWebView;
    private WebEntity mWebViewEntity;
    private WebSettings mWebSetting;
    private int mPackageNum = 0;
    private boolean isBack = false;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mWebViewEntity = getIntent().getParcelableExtra(Constance.KEY.PARCELABLE_ENTITY);
        initWidget();
    }

    private void initWidget() {
        mWebView.setWebViewClient(getWebViewClient());
        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        mWebSetting.setDomStorageEnabled(true);
        mWebSetting.setLoadWithOverviewMode(false);
        mWebSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "fjz");
        mWebView.setWebChromeClient(getWebChromeClient());
        loadUrl(mWebViewEntity.getContentUrl());
        String title = mWebViewEntity.getTitle();
        setTitle(TextUtils.isEmpty(title) ? "网页浏览" : title);
    }

    /**
     * 给子类提供加载网页的接口
     *
     * @param url
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public WebView getmWebView() {
        return mWebView;
    }

    public WebSettings getWebSetting() {
        return mWebSetting;
    }

    public void setWebSetting(WebSettings mWebSetting) {
        this.mWebSetting = mWebSetting;
    }

    private WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                L.d(TAG, "newProgress ==> " + newProgress);
                if (newProgress > 80) {
                    hintTempView();
                }
            }

        };
    }

    protected void back() {
        if (mWebView.canGoBack() && mPackageNum > 1) {
            mWebView.goBack();
            mPackageNum--;
            isBack = true;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mPackageNum++;
                showWebLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isBack = false;
                dismissWebLoadDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                dismissWebLoadDialog();
            }
        };
    }

    private void showWebLoadingDialog() {
        if (!isBack) {
            showTempView(ITempView.LOADING);
        }
    }

    private void dismissWebLoadDialog() {
        hintTempView();
    }
}
