package com.touchrom.fanjianzhi.entity;


import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2015/12/1.
 * 设置实体
 */
public class SettingEntity extends BaseEntity {
    private boolean showImg = true;
    private String lastCheckTime = "";

    public String getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(String lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public boolean isShowImg() {
        return showImg;
    }

    public void setShowImg(boolean showImg) {
        this.showImg = showImg;
    }
}
