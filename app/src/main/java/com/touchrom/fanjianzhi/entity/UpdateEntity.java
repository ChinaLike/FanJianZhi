package com.touchrom.fanjianzhi.entity;

import com.google.gson.annotations.SerializedName;
import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2015/11/12.
 * 启动实体
 */
public class UpdateEntity extends BaseEntity {
    /**
     * 是否有升级
     */
    @SerializedName("update")
    private boolean hasNewVersion = false;
    /**
     * 版本升级标题
     */
    private String versionTitle;
    /**
     * 版本升级信息
     */
    private String versionMsg;
    /**
     * app下载路径
     */
    private String downloadUrl;
    /**
     * 强制升级
     */
    private boolean forcedUpdate = false;
    /**
     * 版本号
     */
    private String versionCode = "";
    /**
     * 版本名
     */
    private String versionName = "";

    public boolean isHasNewVersion() {
        return hasNewVersion;
    }

    public String getVersionTitle() {
        return versionTitle;
    }

    public String getVersionMsg() {
        return versionMsg;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean isForcedUpdate() {
        return forcedUpdate;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }
}
