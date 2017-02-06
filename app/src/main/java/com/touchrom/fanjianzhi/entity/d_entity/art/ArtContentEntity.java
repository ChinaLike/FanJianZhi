package com.touchrom.fanjianzhi.entity.d_entity.art;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.google.gson.annotations.SerializedName;
import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/6/7.
 */
public class ArtContentEntity extends AbsDEntity {

    int id;         //文章id
    int artType;    //文章类型
    int authorId;   //作者Id
    String category;    //分组
    String title;
    String imgUrl;
    @SerializedName("nikeName")
    String authorName;  //作者
    String content;
    String time;
    int commentNum;
    int starNum;
    int unStarNum;
    int scanNum;
    String sign;
    boolean isFollow;
    boolean isStared;
    boolean isUnStared;

    public int getAuthorId() {
        return authorId;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public void setStared(boolean stared) {
        isStared = stared;
    }

    public void setUnStared(boolean unStared) {
        isUnStared = unStared;
    }

    public boolean isStared() {
        return isStared;
    }

    public boolean isUnStared() {
        return isUnStared;
    }

    public void setUnStarNum(int unStarNum) {
        this.unStarNum = unStarNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public String getSign() {
        return sign;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.ART_CONTENT_ITEM;
    }

    public int getId() {
        return id;
    }

    public int getArtType() {
        return artType;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
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
}
