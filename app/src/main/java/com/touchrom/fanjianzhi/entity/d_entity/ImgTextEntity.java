package com.touchrom.fanjianzhi.entity.d_entity;

import com.arialyy.absadapter.delegate.AbsDEntity;

/**
 * Created by lyy on 2016/6/6.
 */
public class ImgTextEntity extends AbsDEntity {
    int artType;
    int type;
    String imgUrl;  //图片链接
    String detail;  //内容简介
    int id;
    String title;   //标题
    int commentNum; //评论数
    int starNum;    //点赞数
    int unStarNum;  //踩数
    int scanNum;    //浏览数
    String videoUrl;    //视频链接
    String url;     //url跳转链接
    int jump;       //url跳转类型 ==> 0：系统浏览器，1：内置浏览器，2：内页文章
    String nikeName;
    String time;
    String tagName;    //分类名
    int num;            //新内容数量

    public int getNum() {
        return num;
    }

    boolean playGif = false;
    boolean isGif = false;

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

    public boolean isPlayGif() {
        return playGif;
    }

    public void setPlayGif(boolean playGif) {
        this.playGif = playGif;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTime() {
        return time;
    }

    public String getNikeName() {
        return nikeName;
    }

    @Override
    public int getAbsType() {
        return type;
    }

    public int getArtType() {
        return artType;
    }

    public int getType() {
        return type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDetail() {
        return detail;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public int getScanNum() {
        return scanNum;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getUrl() {
        return url;
    }

    public int getJump() {
        return jump;
    }
}
