package com.touchrom.fanjianzhi.entity.d_entity.msg;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/6/15.
 * 消息中心实体
 */
public class MsgCenterEntity extends MsgDelegateEntity {
    int img;
    String title;
    boolean hasNewMsg;
    String detail;
    String time;


    @Override
    public int getAbsType() {
        return Constance.ADAPTER.MSG_SYS;
    }

    public MsgCenterEntity setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public MsgCenterEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public boolean isHasNewMsg() {
        return hasNewMsg;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }
}
