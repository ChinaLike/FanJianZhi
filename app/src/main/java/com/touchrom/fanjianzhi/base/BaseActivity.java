package com.touchrom.fanjianzhi.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.arialyy.frame.core.AbsActivity;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.temp.TempView;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.ReflectionUtil;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.dialog.LoadingDialog;
import com.touchrom.fanjianzhi.dialog.MsgDialog;
import com.touchrom.fanjianzhi.entity.SettingEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.net.ServiceUtil;
import com.touchrom.fanjianzhi.widget.ToolBar;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lyy on 2016/5/31.
 */
public abstract class BaseActivity<VB extends ViewDataBinding> extends AbsActivity<VB> implements ToolBar.OnToolBarClickListener {
    private MsgDialog mMsgDialog;
    private LoadingDialog mLoadingDialog;
    private boolean isSetBarColor = true;
    protected SettingEntity mSetting;
    protected ToolBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (AndroidVersionUtil.hasLollipop()) {
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            Fade in = new Fade(Fade.IN);
//            in.setDuration(1000);
//            Fade out = new Fade(Fade.OUT);
//            out.setDuration(1000);
//            getWindow().setExitTransition(in);
//            getWindow().setEnterTransition(out);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Slide(Gravity.LEFT));
            getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        }
        super.onCreate(savedInstanceState);
        if (AndroidVersionUtil.hasLollipop()) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onBackClick(View view) {
        finish();
    }

    @Override
    public void onRightBtClick(View view) {

    }

    @Override
    public void onMenuClick(View view) {

    }

    /**
     * 显示消息对话框
     *
     * @param title
     * @param msg
     */
    protected void showMsgDialog(final String title, final String msg, final int requestCode) {
        Observable.just("").subscribeOn(Schedulers.io())
                .map(new Func1<String, MsgDialog>() {

                    @Override
                    public MsgDialog call(String s) {
                        return new MsgDialog(title, msg, BaseActivity.this, requestCode);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MsgDialog>() {
                    @Override
                    public void call(MsgDialog msgDialog) {
                        mMsgDialog = msgDialog;
                        msgDialog.show(getSupportFragmentManager(), "msgDialog");
                    }
                });
    }

    /**
     * 显示消息对话框
     *
     * @param title
     * @param msg
     */
    protected void showMsgDialog(final String title, final String msg) {
        showMsgDialog(title, msg, -1);
    }

    /**
     * 关闭消息对话框
     */
    protected void dismissMsgDialog() {
        Observable.just("").delay(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (mMsgDialog != null) {
                            mMsgDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 显示加载等待对话框
     */
    protected void showLoadingDialog() {
        showLoadingDialog(true);
    }

    /**
     * 显示加载等待对话框
     *
     * @param canCancel 能被取消？
     */
    protected void showLoadingDialog(final boolean canCancel) {
        mLoadingDialog = new LoadingDialog(canCancel);
        mLoadingDialog.show(getSupportFragmentManager(), "loadingDialog");
    }

    /**
     * 关闭加载等待对话框，保证对话框至少显示1秒
     */
    protected void dismissLoadingDialog() {
//        dismissLoadingDialog(1000);
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

//    Subscription subscription;
//
//    protected void dismissLoadingDialog(int delay) {
//        if (mLoadingDialog != null) {
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    mLoadingDialog.dismiss();
////                }
////            }, delay);
//            subscription = Observable.just(mLoadingDialog).delay(delay, TimeUnit.MILLISECONDS).subscribe(new Action1<LoadingDialog>() {
//                @Override
//                public void call(LoadingDialog loadingDialog) {
//                    loadingDialog.dismiss();
//                }
//            });
//
//        }
//    }

    @Override
    protected void onDestroy() {
//        if (subscription != null) {
//            subscription.unsubscribe();
//        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mBar = (ToolBar) findViewById(R.id.tool_bar);
        if (mBar != null) {
            mBar.setOnToolBarClickListener(this);
        }
        themeColorSetting(getResources().getColor(R.color.colorPrimaryDark));
        mSetting = AppManager.getInstance().getSetting();
        if (getTempView() != null) {
            ((TempView) getTempView()).setLoadingAnimation(R.drawable.fjz_load_anim);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mBar != null) {
            mBar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@link #isSetBarColor}
     *
     * @param set
     */
    protected void setBarCanSetColor(boolean set) {
        isSetBarColor = set;
    }

    /**
     * 主题颜色设置
     *
     * @param color
     */
    protected void themeColorSetting(int color) {
        if (color == -1 || !isSetBarColor) {
            return;
        }
        //设置4.4的沉浸效果
//        StatusBarCompat.compat(BaseActivity.this, color);
        if (AndroidVersionUtil.hasLollipop()) {
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    public void onBtTempClick(View view, int type) {
        super.onBtTempClick(view, type);
        if (type == TempView.ERROR) {
            onNetError();
        } else if (type == TempView.DATA_NULL) {
            onNetDataNull();
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (obj == null) {
            showTempView(ITempView.DATA_NULL);
        } else if (obj instanceof Integer) {
            int i = (int) obj;
            if (i == ServiceUtil.ERROR) {
                showTempView(ITempView.ERROR);
            }
        } else if (obj instanceof List) {
            List l = (List) obj;
            if (l.size() == 0) {
                showTempView(ITempView.DATA_NULL);
            } else {
                hintTempView(1000);
            }
        } else {
            hintTempView(1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void finish() {
        super.finish();
        if (!AndroidVersionUtil.hasLollipop()) {
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }

    /**
     * 网络数据返回为空
     */
    protected void onNetDataNull() {

    }

    /**
     * 网络错误
     */
    protected void onNetError() {

    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
//        super.startActivity(intent, options);
        if (AndroidVersionUtil.hasLollipop()) {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
            super.startActivity(intent, options);
        } else {
            super.startActivity(intent, options);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
//        super.startActivityForResult(intent, requestCode, options);
        if (AndroidVersionUtil.hasLollipop()) {
            if (options == null) {
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
            }
            super.startActivityForResult(intent, requestCode, options);
        } else {
            super.startActivityForResult(intent, requestCode, options);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

}
