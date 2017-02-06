package com.touchrom.fanjianzhi.help.turn;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.touchrom.fanjianzhi.activity.web.BaseWebView;
import com.touchrom.fanjianzhi.activity.web.NormalWebView;
import com.touchrom.fanjianzhi.base.BaseEntity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.entity.WebEntity;
import com.touchrom.fanjianzhi.help.turn.inf.ITurn;

/**
 * Created by lyy on 2015/8/14.
 * 跳转到具体页面
 */
final class TurnWeb extends TurnHelp implements ITurn {
    private Context mContext;
    private WebEntity mEntity;

    private static volatile TurnWeb TURN_WEB = null;
    private static final Object LOCK = new Object();

    public static TurnWeb getInStance(Context context) {
        if (TURN_WEB == null) {
            synchronized (LOCK) {
                TURN_WEB = new TurnWeb(context);
            }
        }
        return TURN_WEB;
    }

    private TurnWeb(Context context) {
        super(context);
    }

    @Override
    public void onTurn(Context context, int appId) {

    }

    @Override
    public void onTurn(Context context, BaseEntity entity) {
        mContext = context;
        mEntity = (WebEntity) entity;
        onNormalWeb(mEntity);
    }

    @Override
    public void onTurn(Context context, BaseEntity entity, View itemView, View rootView) {

    }

    public void onNormalWeb(WebEntity entity) {
        Intent intent = new Intent(mContext, NormalWebView.class);
        intent.putExtra(Constance.KEY.PARCELABLE_ENTITY, entity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void onViewWeb(WebEntity entity) {
        Intent intent = new Intent(mContext, BaseWebView.class);
        intent.putExtra(Constance.KEY.PARCELABLE_ENTITY, mEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void onPayWeb() {
    }

    public void onShareWeb() {

    }

}
