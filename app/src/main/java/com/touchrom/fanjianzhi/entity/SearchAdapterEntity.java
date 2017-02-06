package com.touchrom.fanjianzhi.entity;

import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2016/6/15.
 * 搜索实体
 */
public class SearchAdapterEntity extends BaseEntity {
    CharSequence text;
    int type;
    int img;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
