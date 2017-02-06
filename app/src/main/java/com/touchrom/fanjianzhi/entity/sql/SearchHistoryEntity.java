package com.touchrom.fanjianzhi.entity.sql;

import org.litepal.crud.DataSupport;

/**
 * Created by lyy on 2016/6/6.
 */
public class SearchHistoryEntity extends DataSupport {
    private String title;
    private String detail;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
