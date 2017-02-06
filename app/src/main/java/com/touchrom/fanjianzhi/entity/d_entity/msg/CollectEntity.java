package com.touchrom.fanjianzhi.entity.d_entity.msg;

import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/6/16.
 * 收藏实体
 */
public class CollectEntity extends MsgDelegateEntity {
    int id;
    String nikeName;
    String artTitle;

    public int getId() {
        return id;
    }

    String imgUrl;

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.MSG_COLLECT;
    }

    public String getNikeName() {
        return nikeName;
    }

    public String getArtTitle() {
        return artTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public int getMsgId() {
        return id;
    }
}
