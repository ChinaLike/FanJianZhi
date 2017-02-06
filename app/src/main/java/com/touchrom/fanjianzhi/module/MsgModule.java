package com.touchrom.fanjianzhi.module;

import android.content.Context;
import android.text.TextUtils;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.entity.d_entity.msg.CallMeEntity;
import com.touchrom.fanjianzhi.entity.d_entity.msg.CollectEntity;
import com.touchrom.fanjianzhi.entity.d_entity.msg.MsgCenterEntity;
import com.touchrom.fanjianzhi.entity.d_entity.msg.ReplayEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lyy on 2016/6/15.
 */
public class MsgModule extends BaseModule {
    public MsgModule(Context context) {
        super(context);
    }

    /**
     * 删除收藏
     */
    public void delCollect(String collectIds) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("ids", collectIds);
        mServiceUtil.cancelCollect(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    T.showShort(getContext(), "移除收藏" + (mServiceUtil.isRequestSuccess(obj) ? "成功" : "失败"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 删除消息
     *
     * @param type 1回复，2@，3系统
     */
    public void delMsg(int type, String msgId) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("type", type + "");
        params.put("ids", msgId);
        mServiceUtil.delMsg(params, new HttpUtil.AbsResponse());
    }

    /**
     * 获取数据
     *
     * @param type Constance.ADAPTER.MSG_REPLAY, Constance.ADAPTER.MSG_SYS,
     *             Constance.ADAPTER.MSG_CALL, Constance.ADAPTER.MSG_COLLECT
     * @param page
     */
    public void getData(int type, int page) {
        switch (type) {
            case Constance.ADAPTER.MSG_CALL:
                getCallMeData(page);
                break;
            case Constance.ADAPTER.MSG_REPLAY:
                getReplayData(page);
                break;
            case Constance.ADAPTER.MSG_SYS:
                getSysData(page);
                break;
            case Constance.ADAPTER.MSG_COLLECT:
                getCollectData(page);
                break;
        }
    }

    /**
     * 获取收藏数据
     */
    public void getCollectData(int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("limit", "10");
        params.put("page", page + "");
        mServiceUtil.getCollectData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<CollectEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<CollectEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MSG_DATA, lists);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MSG_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取系统消息数据
     */
    public void getSysData(int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("limit", "10");
        params.put("page", page + "");
        mServiceUtil.getSysData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MsgCenterEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MsgCenterEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MSG_DATA, lists);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MSG_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取@我的数据
     */
    public void getCallMeData(int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("limit", "10");
        params.put("page", page + "");
        mServiceUtil.getCallMeData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<CallMeEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<CallMeEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MSG_DATA, lists);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MSG_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取收到的回复数据
     */
    public void getReplayData(int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("limit", "10");
        params.put("page", page + "");
        mServiceUtil.getMsgReplayData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<ReplayEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<ReplayEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_MSG_DATA, lists);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_MSG_DATA, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 获取消息中心数据
     */
    public void getMsgCenterData() {
        Map<String, String> params = new WeakHashMap<>();
        mServiceUtil.getMsgCenterData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MsgCenterEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MsgCenterEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                    callback(ResultCode.SERVER.GET_MSG_CENTER_DATA, lists);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback(ResultCode.SERVER.GET_MSG_CENTER_DATA, null);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
            }
        });
    }
}
