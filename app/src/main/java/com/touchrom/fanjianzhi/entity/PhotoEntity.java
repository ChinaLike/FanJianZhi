package com.touchrom.fanjianzhi.entity;

import com.touchrom.fanjianzhi.base.BaseEntity;

/**
 * Created by lyy on 2016/6/2.
 */
public class PhotoEntity extends BaseEntity {
    int type;
    String imgUrl;
    String title;
    String detail;
    String url;
    int jump;
    int id;
    String videoUrl;
    int artType;
    int scanNum;
    int commentNum;
    int starNum;
    int unStarNum;

    public int getType() {
        return type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getUrl() {
        return url;
    }

    public int getJump() {
        return jump;
    }

    public int getId() {
        return id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getArtType() {
        return artType;
    }

    public int getScanNum() {
        return scanNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public int getStarNum() {
        return starNum;
    }

    public int getUnStarNum() {
        return unStarNum;
    }
}
