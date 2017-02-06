package com.touchrom.fanjianzhi.entity.d_entity.msg;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/6/15.
 *
 * @我的消息实体
 */
public class CallMeEntity extends MsgDelegateEntity {
    String imgUrl;
    String nikeName;
    String time;
    String content;
    String artTitle;
    int userId;

    int commentId;  //评论Id

    public int getCommentId() {
        return commentId;
    }

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.MSG_CALL;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getArtTitle() {
        return artTitle;
    }

    public int getUserId() {
        return userId;
    }
}
