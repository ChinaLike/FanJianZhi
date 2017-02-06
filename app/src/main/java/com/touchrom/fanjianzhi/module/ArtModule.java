package com.touchrom.fanjianzhi.module;

import android.content.Context;

import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.ShareEntity;
import com.touchrom.fanjianzhi.entity.UserEntity;
import com.touchrom.fanjianzhi.entity.d_entity.art.ArtContentEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lyy on 2016/6/7.
 * 文章详情模型
 */
public class ArtModule extends BaseModule {
    public ArtModule(Context context) {
        super(context);
    }

    /**
     * 获取热门评论
     */
    public void getHotComment(int artId) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        mServiceUtil.getHotComment(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<CommentEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<CommentEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_HOT_COMMENT, lists);
                }
            }
        });
    }

    /**
     * 打赏
     *
     * @param type 1,文章
     */
    public void reward(int authorId, int artId, int money, int type) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("toUserId", authorId + "");
        params.put("artId", artId + "");
        params.put("jb", money + "");
        params.put("type", type + "");
        mServiceUtil.reward(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject data1 = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        if (data1.getBoolean("result")) {
                            T.showShort(getContext(), "打赏成功");
                            UserEntity entity = AppManager.getInstance().getUser();
                            entity.setMoney(data1.getInt("jb"));
                            NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, entity);
                            AppManager.getInstance().saveUser(entity);
                        }
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 投诉
     *
     * @param type 1=>"内容违规：存在色情、暴力、反动等内容",
     *             2=>"内容侵权：涉及侵犯他人版权",
     *             3=>"恶意广告：有未明确标注的商业推广行为",
     *             4=>"其它问题"
     */
    public void complaints(int artId, int type) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        params.put("type", type + "");
        params.put("text", "");
        mServiceUtil.complaints(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                T.showShort(getContext(), "投诉成功");
            }
        });
    }

    /**
     * 获取分享数据
     */
    public void getShareData(int artId, int typ) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        params.put("type", typ + "");
        mServiceUtil.getShareData(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                ShareEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), ShareEntity.class);
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (entity != null) {
                        callback(ResultCode.SERVER.GET_SHARE_DATA, entity);
                    }
                }
            }
        });
    }

    /**
     * 收藏
     */
    public void artCollect(int artId) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        mServiceUtil.artCollect(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean state = false;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        state = obj.getJSONObject(ServiceUtil.DATA_KEY).getBoolean("result");
                        T.showShort(getContext(), "收藏成功");
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.COLLECT, state);
                }
            }
        });
    }

    /**
     * 获取收藏状态
     */
    public void getCollectState(int artId) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        mServiceUtil.getCollectState(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean state = false;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        state = obj.getJSONObject(ServiceUtil.DATA_KEY).getBoolean("result");
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_COLLECT_STATE, state);
                }
            }
        });
    }

    /**
     * 回复
     *
     * @param type  1:回复文章，2：回复评论
     * @param artId 评论文章，artId为文章id,评论回复，artId为回复id
     */
    public void comment(int type, int artId, String cmtContent) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        params.put("text", cmtContent);
        params.put("type", type + "");
        mServiceUtil.artComment(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                CommentEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), CommentEntity.class);
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (entity != null) {
                        callback(ResultCode.SERVER.ART_COMMENT, entity);
                    }
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                T.showShort(getContext(), "网络错误");
            }
        });
    }

    /**
     * 获取评论列表
     */
    public void getCommentList(int artId, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        params.put("limit", 10 + "");
        params.put("page", page + "");
        mServiceUtil.getCommentList(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<CommentEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<CommentEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_COMMENT_LIST, lists);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_COMMENT_LIST, ServiceUtil.ERROR);
            }
        });
    }

    /**
     * 相关推荐
     */
    public void getArtRecommend(int artType) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artType", artType + "");
        mServiceUtil.artRecommend(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MainContentListEntity> lists = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MainContentListEntity>>() {
                                }.getType());
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_ART_RECOMMEND, lists);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
            }
        });
    }

    /**
     * 点赞
     *
     * @param type type=1 评论; type=2 文章
     */
    public void star(int artId, int type, HttpUtil.AbsResponse response) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        params.put("type", type + "");
        mServiceUtil.star(params, response);
    }

    /**
     * 踩
     */
    public void unStar(int artId, HttpUtil.AbsResponse response) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        mServiceUtil.unStar(params, response);
    }

    /**
     * 关注
     */
    public void follow(int userId, HttpUtil.AbsResponse response) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("targetUserId", userId + "");
        mServiceUtil.follow(params, response);
    }

    /**
     * 获取文章详情
     *
     * @param artId
     */
    public void getArtDetail(int artId) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("artId", artId + "");
        mServiceUtil.getArtDetail(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                ArtContentEntity artEntity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        artEntity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), ArtContentEntity.class);
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_ART_DETAIL, artEntity);
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.GET_ART_DETAIL, ServiceUtil.ERROR);
            }
        });
    }
}
