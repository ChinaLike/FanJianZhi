package com.touchrom.fanjianzhi.module;

import android.content.Context;
import android.os.Looper;

import com.arialyy.frame.core.NotifyHelp;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.entity.AccountEntity;
import com.touchrom.fanjianzhi.entity.CodeResultEntity;
import com.touchrom.fanjianzhi.entity.UserEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;
import com.touchrom.fanjianzhi.util.Encryption;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by lyy on 2016/6/3.
 * 用户相关的module
 */
public class UserModule extends BaseModule {
    public UserModule(Context context) {
        super(context);
    }

    /**
     * 验证账号
     */
    public void isReg(String account) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("account", account);
        mServiceUtil.isReg(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                boolean isReg = false;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        JSONObject content = obj.getJSONObject(ServiceUtil.DATA_KEY);
                        isReg = content.getBoolean("isReged");
                        if (isReg) {
                            T.showShort(getContext(), mServiceUtil.getMessage(content));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.IS_REG, isReg);
                }
            }
        });
    }

    /**
     * 签到
     */
    public void sign() {
        mServiceUtil.sign(new WeakHashMap<String, String>(), new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        UserEntity entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                        T.showShort(getContext(), "签到成功");
                        AppManager.getInstance().saveUser(entity);
                        NotifyHelp.getInstance().update(Constance.NOTIFY_FLAG.LOGIN, Constance.NOTIFY_ACTION.LOGIN_ACTION, entity);
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
     * 修改密码
     */
    public void modifyPw(final String phone, final String pw) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("password", Encryption.encodeSHA1ToString(pw));
        params.put("mobile", phone);
        mServiceUtil.modifyPw(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                String str = "";
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    } else {
                        str = mServiceUtil.getMsg(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (entity != null) {
                        AppManager.getInstance().saveLoginState(true);
                        AppManager.getInstance().saveUser(entity);
                        AppManager.getInstance().saveAccount(new AccountEntity(phone, pw, false));
                        callback(ResultCode.SERVER.MODIFY_PW, entity);
                    } else {
                        callback(ResultCode.SERVER.MODIFY_PW, str);
                    }
                }
            }
        });
    }


    /**
     * 第三方登录
     *
     * @param libLoginName SinaWeibo.NAME，QQ.NAME，Wechat.NAME
     * @param type         1、QQ登录 2、微信登录 3、微博登录
     */
    public void libLogin(final int type, final String libLoginName) {
        if (!AppManager.getInstance().isLogin()) {
            Platform pf = ShareSDK.getPlatform(libLoginName);
            pf.SSOSetting(false);
            pf.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    showTest("授权成功");
                    PlatformDb db = platform.getDb();
                    getLibLoginEntity(type, libLoginName, db.getToken(), db.getUserId(), db.getUserName(), db.getUserIcon());
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    showTest("授权失败，请重试");
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    showTest("授权取消");
                }
            });
            pf.authorize();
        } else {
            AccountEntity.LibAccount libAccount = AppManager.getInstance().getAccount().getLibAccount();
            getLibLoginEntity(type, libLoginName, libAccount.getToken(), libAccount.getUserId(), libAccount.getUserName(), libAccount.getImgUrl());
        }
    }


    /**
     * 利用第三方登录token获取用户实体
     */
    private void getLibLoginEntity(final int type, final String libLoginName, final String token,
                                   final String userId, final String userName, final String imgUrl) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("type", type + "");
        params.put("token", token);
        params.put("uid", userId);
        params.put("username", userName);
        params.put("url", imgUrl);
        mServiceUtil.libLogin(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (entity != null) {
                        AccountEntity account = new AccountEntity(true);
                        AccountEntity.LibAccount libAccount = new AccountEntity.LibAccount();
                        libAccount.setImgUrl(imgUrl);
                        libAccount.setType(type);
                        libAccount.setToken(token);
                        libAccount.setUserId(userId);
                        libAccount.setUserName(userName);
                        libAccount.setLibLoginName(libLoginName);
                        account.setLibAccount(libAccount);
                        AppManager.getInstance().saveAccount(account);
                        AppManager.getInstance().saveUser(entity);
                        AppManager.getInstance().saveLoginState(true);
                    }
                    callback(ResultCode.SERVER.LIB_LOGIN, entity);
                }
            }
        });
    }

    private void showTest(final String msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.myLooper();
                T.showShort(getContext(), msg);
                Looper.loop();
            }
        }).start();
    }

    /**
     * 进行注册
     */
    public void reg(final String phoneNum, final String nikeName, final String password) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("mobile", phoneNum);
        params.put("nikeName", nikeName);
        params.put("password", Encryption.encodeSHA1ToString(password));
        mServiceUtil.reg(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                String str = "";
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    } else {
//                        T.showShort(getContext(), str);
                        str = mServiceUtil.getMsg(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (entity != null) {
                        AppManager.getInstance().saveLoginState(true);
                        AppManager.getInstance().saveUser(entity);
                        AppManager.getInstance().saveAccount(new AccountEntity(phoneNum, password, false));
                        callback(ResultCode.SERVER.REG, entity);
                    } else {
                        callback(ResultCode.SERVER.REG, str);
                    }
                }
            }
        });
    }

    /**
     * 验证短信验证码
     */
    public void confirmMsgCode(String phone, String msgCode) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("phone", phone);
        params.put("code", msgCode);
        mServiceUtil.confirmMsgCode(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        callback(ResultCode.SERVER.CONFIRM_MSG_CODE, new Gson().fromJson(
                                obj.getJSONObject(ServiceUtil.DATA_KEY)
                                        .toString(), CodeResultEntity.class));
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
     * 验证图片验证码
     */
    public void confirmImgCode(String phone, String imgCode) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("code", imgCode);
        params.put("phone", phone);
        mServiceUtil.confirmImgCode(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        CodeResultEntity entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY)
                                .toString(), CodeResultEntity.class);
                        callback(ResultCode.SERVER.CONFIRM_IMG_CODE, entity);
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
     * 获取验证码
     *
     * @param type 1:注册, 2:找回密码
     */
    public void getCode(String phoneNum, int type) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("phone", phoneNum);
        params.put("type", type + "");
        mServiceUtil.getMsgCode(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        T.showShort(getContext(), "获取验证码成功");
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
     * 登录
     *
     * @param userName
     * @param pw
     */
    public void login(final String userName, final String pw) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("userName", userName);
        params.put("password", Encryption.encodeSHA1ToString(pw));
        mServiceUtil.login(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                UserEntity entity = null;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        entity = new Gson().fromJson(obj.getJSONObject(ServiceUtil.DATA_KEY).toString(), UserEntity.class);
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (entity != null) {
                        AppManager.getInstance().saveLoginState(true);
                        AppManager.getInstance().saveUser(entity);
                        AppManager.getInstance().saveAccount(new AccountEntity(userName, pw, false));
                    }
                    callback(ResultCode.SERVER.LOGIN, entity);
                }
            }
        });
    }

}
