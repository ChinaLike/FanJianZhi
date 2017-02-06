package com.touchrom.fanjianzhi.entity;

import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2016/6/8.
 * 验证码返回实体
 */
public class CodeResultEntity extends BaseEntity {
    boolean isSuccess;
    boolean isShowImgCode = false;
    String imgUrl;

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setShowImgCode(boolean showImgCode) {
        isShowImgCode = showImgCode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isShowImgCode() {
        return isShowImgCode;
    }
}
