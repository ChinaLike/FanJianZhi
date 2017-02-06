package com.touchrom.fanjianzhi.help.turn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.WindowManager;

import com.touchrom.fanjianzhi.activity.ArtActivity;
import com.touchrom.fanjianzhi.activity.ChannelActivity;
import com.touchrom.fanjianzhi.activity.web.NormalWebView;
import com.touchrom.fanjianzhi.activity.web.VideoWebView;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.WebEntity;
import com.touchrom.fanjianzhi.entity.d_entity.BannerEntity;


/**
 * Created by lyy on 2015/8/14.
 * 跳转帮助类
 */
public class TurnHelp {
    private static volatile TurnHelp INSTANCE;
    private static final Object LOCK = new Object();
    protected WindowManager mWm;
    protected WindowManager.LayoutParams mWmParams;

    protected TurnHelp(Context context) {
        if (mWm == null) {
            mWm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            mWmParams = new WindowManager.LayoutParams();
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;     // 系统提示类型,重要
            mWmParams.format = 1;
            mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL; // 排版不受限制
            mWmParams.alpha = 1.0f;
            mWmParams.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角
            //以屏幕左上角为原点，设置x、y初始值
            mWmParams.x = 0;
            mWmParams.y = 0;
            //设置悬浮窗口长宽数据
            mWmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mWmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        }
    }

    public static TurnHelp getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new TurnHelp();
            }
        }
        return INSTANCE;
    }

    private TurnHelp() {
    }

    /**
     * Banner跳转
     */
    public void turnBanner(Context context, BannerEntity bannerEntity) {
        switch (bannerEntity.getJump()) {
            case 0: //系统浏览器
                turnSysWeb(context, bannerEntity.getUrl());
                break;
            case 1: //内置浏览器
                WebEntity entity = new WebEntity(bannerEntity.getUrl());
                turnNormalWeb(context, entity);
                break;
            case 2: //文章
                turnArt(context, bannerEntity.getArticleId());
                break;
        }
    }

    /**
     * 跳转文章
     */
    public void turnArt(Context context, int artId) {
        Intent intent = new Intent(context, ArtActivity.class);
        intent.putExtra(Constance.KEY.ID, artId);
        context.startActivity(intent);
    }

    /**
     * 跳转外置浏览器
     */
    public void turnSysWeb(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 首页列表跳转
     */
    public void artListTurn(Context context, MainContentListEntity entity) {
        Intent intent = null;
        int id = -1;
        switch (entity.getType()) {
            case Constance.ADAPTER.CHANNEL_CONTENT_ITEM:
                id = entity.getChannelContent().getId();
                intent = new Intent(context, ChannelActivity.class);
                intent.putExtra(Constance.KEY.STRING, entity.getChannelContent().getDetail());
                break;
            case Constance.ADAPTER.IMG_CONTENT_ITEM:
                id = entity.getImgContent().getId();
                turnArt(context, id);
                break;
            case Constance.ADAPTER.IMG_TEXT_CONTENT_ITEM:
                id = entity.getImgTextContent().getId();
                turnArt(context, id);
                break;
            case Constance.ADAPTER.VIDEO_CONTENT_ITEM:
                id = entity.getVideoContent().getId();
                turnArt(context, id);
                break;
            case Constance.ADAPTER.TEXT_CONTENT_ITEM:
                id = entity.getTextContent().getId();
                turnArt(context, id);
                break;
        }

        if (intent != null) {
            intent.putExtra(Constance.KEY.ID, id);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到普通的Web页面
     */
    public void turnNormalWeb(Context context, WebEntity entity) {
        Intent intent = new Intent(context, NormalWebView.class);
        intent.putExtra(Constance.KEY.PARCELABLE_ENTITY, entity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转视频播放的Web
     */
    public void turnVideoWeb(Context context, WebEntity entity) {
        Intent intent = new Intent(context, VideoWebView.class);
        intent.putExtra(Constance.KEY.PARCELABLE_ENTITY, entity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
