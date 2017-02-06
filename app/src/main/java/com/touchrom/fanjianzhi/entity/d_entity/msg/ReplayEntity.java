package com.touchrom.fanjianzhi.entity.d_entity.msg;

import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/6/15.
 * 收到的回复实体
 */
public class ReplayEntity extends MsgDelegateEntity {
    String imgUrl;
    String cmNikeName;
    int cmUserId;    //回复者Id
    String cmContent;   //回复者的回复内容
    String myNikeName;  //我的用户昵称
    String myCmContent;
    String artTitle;
    String time;
    int commentId;  //评论Id

    public int getCommentId() {
        return commentId;
    }

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.MSG_REPLAY;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCmNikeName() {
        return cmNikeName;
    }

    public int getCmUserId() {
        return cmUserId;
    }

    public String getCmContent() {
        return cmContent;
    }

    public String getMyNikeName() {
        return myNikeName;
    }

    public String getMyCmContent() {
        return myCmContent;
    }

    public String getArtTitle() {
        return artTitle;
    }

    public String getTime() {
        return time;
    }
}
