package com.touchrom.fanjianzhi.entity.sql;

import org.litepal.crud.DataSupport;

/**
 * Created by lyy on 2016/6/27.
 */
public class LargeImgInfoEntity extends DataSupport {
    String imgUrl;
    int len;
    long time;
    boolean isCompleted = false;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
