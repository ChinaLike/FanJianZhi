package com.touchrom.fanjianzhi.net;

import android.content.Context;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.config.ServerConstance;
import com.touchrom.fanjianzhi.entity.UserEntity;
import com.touchrom.fanjianzhi.util.Encryption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lyy on 2015/11/11.
 * 网络接口请求类
 */
public class ServiceUtil {
    private Context mContext;
    private HttpUtil mHttpUtil;
    private volatile static ServiceUtil INSTANCE = null;
    private static final Object LOCK = new Object();
    public static final String DATA_KEY = "content";
    /**
     * 网络错误
     */
    public static final int ERROR = 0xdff1;
    /**
     * 数据为空
     */
    public static final int NULL = 0xdff2;

    private ServiceUtil() {
    }

    private ServiceUtil(Context context) {
        mContext = context;
        mHttpUtil = HttpUtil.getInstance(context);
    }

    public static ServiceUtil getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceUtil(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 验证账号
     */
    public void isReg(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.IS_REG, map, response, true);
    }

    /**
     * 获取热门评论
     */
    public void getHotComment(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_HOT_COMMENT, map, response, true);
    }

    /**
     * 获取收藏列表
     */
    public void getCollectData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COLLECT_DATA, map, response, true);
    }

    /**
     * 删除收藏
     */
    public void delMsg(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.DEL_MSG, map, response, false);
    }

    /**
     * 收到的回复
     */
    public void getMsgReplayData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MSG_REPLAY, map, response, true);
    }

    /**
     * 获取@我的消息
     */
    public void getCallMeData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MSG_CALL_ME, map, response, true);
    }

    /**
     * 获取系统消息
     */
    public void getSysData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MSG_SYS, map, response, true);
    }

    /**
     * 打赏
     */
    public void reward(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.REWARD, map, response, false);
    }

    /**
     * 取消收藏
     */
    public void cancelCollect(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CANCEL_COLLECT, map, response, false);
    }

    /**
     * 投诉
     */
    public void complaints(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.COMPLAINTS, map, response, false);
    }

    /**
     * 签到
     */
    public void sign(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SIGN, map, response, false);
    }

    /**
     * 关注
     */
    public void follow(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.FOLLOW, map, response, false);
    }

    /**
     * 获取消息中心数据
     */
    public void getMsgCenterData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_MSG_CENTER_DATA, map, response, true);
    }

    /**
     * 获取分享数据
     */
    public void getShareData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_SHARE_DATA, map, response, true);
    }

    /**
     * 收藏
     */
    public void artCollect(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.COLLECT, map, response, false);
    }

    /**
     * 获取收藏状态
     */
    public void getCollectState(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COLLECT_STATE, map, response, false);
    }

    /**
     * 文章评论
     */
    public void artComment(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.ART_COMMENT, map, response, false);
    }

    /**
     * 评论列表
     */
    public void getCommentList(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_COMMENT, map, response, true);
    }

    /**
     * 文章推荐
     */
    public void artRecommend(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.ART_RECOMMEND, map, response, true);
    }

    /**
     * 踩
     */
    public void unStar(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.UN_STAR, map, response, false);
    }

    /**
     * 点赞
     */
    public void star(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.STAR, map, response, false);
    }

    /**
     * 修改密码
     */
    public void modifyPw(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MODIFY_PW, map, response, false);
    }

    /**
     * 搜索内容
     */
    public void searchContent(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEARCH_CONTENT, map, response, true);
    }

    /**
     * 搜索关键字
     */
    public void searchKey(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.SEARCH_KEY, map, response, true);
    }

    /**
     * 第三方登录
     */
    public void libLogin(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.LIB_LOGIN, map, response, true);
    }

    /**
     * 注册
     */
    public void reg(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.REG, map, response, false);
    }

    /**
     * 验证图片验证码
     */
    public void confirmImgCode(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CONFIRM_IMG_CODE, map, response, false);
    }

    /**
     * 验证短信验证码
     */
    public void confirmMsgCode(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CONFIRM_MSG_CODE, map, response, false);
    }

    /**
     * 获取短信验证码
     */
    public void getMsgCode(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.GET_MSG_CODE, map, response, false);
    }

    /**
     * 登录
     */
    public void login(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.LOGIN, map, response, true);
    }

    /**
     * 获取文章详情
     */
    public void getArtDetail(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.ART_INFO, map, response, true);
    }

    /**
     * 频道下文章列表
     */
    public void getChannelArtList(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.CHANNEL_ART_LIST, map, response, true);
    }

    /**
     * 获取首页文章列表
     */
    public void getMainTabContentList(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MAIN_TAB_CONTENT_LIST, map, response, true);
    }

    /**
     * 栏目
     */
    public void getTabData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.MAIN_TAB, map, response, true);
    }

    /**
     * 版本升级
     */
    public void update(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.UPDATE, map, response);
    }

    /**
     * 获取启动实体
     */
    public void getLauncherData(Map<String, String> map, HttpUtil.AbsResponse response) {
        requestData(ServerConstance.LAUNCHER, map, response, true);
    }


    /**
     * 判断是否请求成功
     *
     * @param jsonObject
     * @return
     */
    public boolean isRequestSuccess(JSONObject jsonObject) {
        try {
            return jsonObject.getInt("code") == 200;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param obj   JsonObj
     * @param clazz
     * @param <T>   各种实体
     * @return
     * @throws JSONException
     */
    public <T> List<T> jsonArray2List(JSONObject obj, Class<T> clazz) throws JSONException {
        return new Gson().fromJson(obj.getJSONArray("content").toString(),
                new TypeToken<List<T>>() {
                }.getType());
    }

    /**
     * 获取msg
     *
     * @param jsonObject
     * @return
     */
    public String getMsg(JSONObject jsonObject) {
        try {
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getMessage(JSONObject jsonObject) {
        try {
            return jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

//    private static Map<String, String> mixParams(Map<String, String> map) {
//        Map<String, String> params = new HashMap<>();
//        Set set = map.entrySet();
//        for (Object aSet : set) {
//            Map.Entry entry = (Map.Entry) aSet;
//            params.put(entry.getKey() + "", entry.getValue() + "");
//        }
//        //添加通用参数
//        return params;
//    }

    /**
     * 文件上传
     *
     * @param url
     * @param key      和服务器匹配的key
     * @param filePath
     * @param response
     */
    private void upload(String url, String key, String filePath, HttpUtil.AbsResponse response) {
//        mHttpUtil.uploadFile(ServerConstance.BASE_URL + url, filePath, key, HttpUtil.CONTENT_TYPE_IMG, createHeadr(), response);
        mHttpUtil.uploadFile(ServerConstance.TEST_BASE_URL + url, filePath, key, HttpUtil.CONTENT_TYPE_IMG, createHeader(), response);
    }

    /**
     * 不使用缓存的网络请求
     */
    private void requestData(String url, Map<String, String> params, HttpUtil.AbsResponse response) {
        requestData(url, params, response, false);
    }

    /**
     * @param url      请求链接
     * @param params   请求参数
     * @param response 网络回调接口
     * @param useCache 是否使用缓存
     */
    private void requestData(String url, Map<String, String> params, HttpUtil.AbsResponse response, boolean useCache) {
        mHttpUtil.post(ServerConstance.BASE_URL + url, params, createHeader(), response, useCache);
//        mHttpUtil.post(ServerConstance.TEST_BASE_URL + url, params, createHeader(), response, useCache);
    }

    private Map<String, String> createHeader() {
        String randomCode = Encryption.getRandomCode();
        String timestamp = System.currentTimeMillis() + "";
        Map<String, String> headers = new WeakHashMap<>();
        headers.put("appId", "AP628DEB87");
        headers.put("nonce", randomCode);
        headers.put("sign", Encryption.getSingCode(randomCode, timestamp));
        headers.put("timestamp", timestamp);
        headers.put("Charset", "UTF-8");
        headers.put("version", AndroidUtils.getVersionName(mContext));
        headers.put("versionCode", AndroidUtils.getVersionCode(mContext) + "");
        headers.put("device", 2 + "");
        UserEntity user = AppManager.getInstance().getUser();
        if (user != null) {
            headers.put("userId", user.getId() + "");
        }
        return headers;
    }
}
