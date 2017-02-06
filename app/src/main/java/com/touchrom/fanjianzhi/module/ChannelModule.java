package com.touchrom.fanjianzhi.module;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lyy on 2016/6/7.
 * 频道模型
 */
public class ChannelModule extends BaseModule {
    public ChannelModule(Context context) {
        super(context);
    }

    /**
     * 频道文章列表
     * @param channelId 频道id
     * @param tabId     栏目id
     * @param page
     */
    public void getChannelArtList(int channelId, int tabId, int page){
        Map<String, String> params = new WeakHashMap<>();
        params.put("pdId", channelId + "");
        params.put("flTitle", tabId + "");
        params.put("limit", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getChannelArtList(params, new HttpUtil.AbsResponse(){
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
                    callback(ResultCode.SERVER.GET_CHANNEL_ART_LIST, datas);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_CHANNEL_ART_LIST, ServiceUtil.ERROR);
            }
        });
    }
}
