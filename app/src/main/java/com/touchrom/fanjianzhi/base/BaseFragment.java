package com.touchrom.fanjianzhi.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arialyy.frame.core.AbsFragment;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.temp.TempView;
import com.arialyy.frame.util.show.L;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.dialog.LoadingDialog;
import com.touchrom.fanjianzhi.dialog.MsgDialog;
import com.touchrom.fanjianzhi.entity.SettingEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.net.ServiceUtil;
import com.umeng.analytics.MobclickAgent;

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
public abstract class BaseFragment<VB extends ViewDataBinding> extends AbsFragment<VB> {
    private LoadingDialog mLoadingDialog;
    private MsgDialog mMsgDialog;

    protected SettingEntity mSettingEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSettingEntity = AppManager.getInstance().getSetting();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getTempView() != null) {
            ((TempView) getTempView()).setLoadingAnimation(R.drawable.fjz_load_anim);
        }
        super.onActivityCreated(savedInstanceState);
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
                        return new MsgDialog(title, msg, BaseFragment.this, requestCode);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MsgDialog>() {
                    @Override
                    public void call(MsgDialog msgDialog) {
                        mMsgDialog = msgDialog;
                        msgDialog.show(mActivity.getSupportFragmentManager(), "msgDialog");
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getContext()).clearMemory();
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
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(canCancel);
        }
        mLoadingDialog.show(getChildFragmentManager(), "loadingDialog");
    }

    /**
     * 关闭加载等待对话框，保证对话框至少显示1秒
     */
    protected void dismissLoadingDialog() {
        dismissLoadingDialog(1000);
//        if (mLoadingDialog != null){
//            mLoadingDialog.dismiss();
//        }
    }
    Subscription subscription;
    protected void dismissLoadingDialog(int delay) {
        if (mLoadingDialog != null) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mLoadingDialog.dismiss();
//                }
//            }, delay);
            if (subscription == null || subscription.isUnsubscribed()){
                subscription = Observable.just(mLoadingDialog).delay(delay, TimeUnit.MILLISECONDS).subscribe(new Action1<LoadingDialog>() {
                    @Override
                    public void call(LoadingDialog loadingDialog) {
                        loadingDialog.dismiss();
                        subscription.unsubscribe();
                    }
                });
            }

        }
    }

    @Override
    public void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
        super.onDestroy();
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
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
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onDelayLoad() {

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
}
