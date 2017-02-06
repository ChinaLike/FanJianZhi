package com.touchrom.fanjianzhi.entity;

import com.arialyy.frame.util.AESEncryption;
import com.touchrom.fanjianzhi.base.BaseEntity;
import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/3/1.
 * 账户实体
 */
public class AccountEntity extends BaseEntity {
    String account;
    String password;
    boolean isLibAccount = false;
    LibAccount libAccount;

    public LibAccount getLibAccount() {
        return libAccount;
    }

    public void setLibAccount(LibAccount libAccount) {
        this.libAccount = libAccount;
    }

    public boolean isLibAccount() {
        return isLibAccount;
    }

    public void setLibAccount(boolean libAccount) {
        isLibAccount = libAccount;
    }

    public AccountEntity(boolean isLibAccount) {
        this.isLibAccount = isLibAccount;
    }

    public AccountEntity(String account, String password, boolean isLibAccount) {
        try {
            this.account = AESEncryption.encryptString(Constance.APP.SEED, account);
            this.password = AESEncryption.encryptString(Constance.APP.SEED, password);
            this.isLibAccount = isLibAccount;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAccount() {
        try {
            return AESEncryption.decryptString(Constance.APP.SEED, account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAccount(String account) {
        try {
            this.account = AESEncryption.encryptString(Constance.APP.SEED, account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPassword() {
        try {
            return AESEncryption.decryptString(Constance.APP.SEED, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPassword(String password) {
        try {
            this.password = AESEncryption.encryptString(Constance.APP.SEED, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class LibAccount {
        //1、QQ登录 2、微信登录 3、微博登录
        int type;
        String token;
        String userId;
        String userName;
        String imgUrl;
        //第三方登录名：SinaWeibo.NAME，QQ.NAME，Wechat.NAME
        String libLoginName;

        public String getLibLoginName() {
            return libLoginName;
        }

        public void setLibLoginName(String libLoginName) {
            this.libLoginName = libLoginName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

    }
}
