package com.touchrom.fanjianzhi.entity;

import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2016/6/1.
 */
public class LauncherEntity extends BaseEntity {
    String imgUrl;
    String adUrl;
    int id;
    int type;

    public String getImgUrl() {
        return imgUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}
