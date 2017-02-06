package com.touchrom.fanjianzhi.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.dialog.VersionUpgradeDialog;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.TabEntity;
import com.touchrom.fanjianzhi.entity.UpdateEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by lyy on 2016/6/2.
 */
public class MainModule extends BaseModule {
    public MainModule(Context context) {
        super(context);
    }

    /**
     * 获取首页文章列表
     */
    public void getMainArtList(int tabId, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("title", tabId + "");
        params.put("limit", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getMainTabContentList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MainContentListEntity> datas = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        datas = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MainContentListEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MAIN_ART_LIST, datas);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MAIN_ART_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取tab数据
     */
    public void getTab() {
        mServiceUtil.getTabData(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<TabEntity> tabs = null;
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        tabs = new Gson().fromJson(object.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<TabEntity>>() {
                                }.getType());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_TAB_DATA, tabs);
                }
            }
        });
    }

    /**
     * 版本升级
     *
     * @param activity
     */
    public void update(final BaseActivity activity) {
        mServiceUtil.update(null, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        UpdateEntity entity = new Gson().fromJson(obj.getString(ServiceUtil.DATA_KEY), UpdateEntity.class);
                        callback(ResultCode.SERVER.CHECK_VERSION, entity);
                        startVersionDialogFlow(entity, activity.getSupportFragmentManager());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 开启版本对话框流程
     */
    private void startVersionDialogFlow(UpdateEntity entity, final FragmentManager fm) {
        Observable.just(entity).observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<UpdateEntity, Boolean>() {
                    @Override
                    public Boolean call(UpdateEntity entity) {
                        return entity.isHasNewVersion();
                    }
                })
                .subscribe(new Action1<UpdateEntity>() {
                    @Override
                    public void call(UpdateEntity entity) {
                        VersionUpgradeDialog dialog = new VersionUpgradeDialog(entity);
                        dialog.show(fm, "versionDialog");
                    }
                });
    }
}
