package com.touchrom.fanjianzhi.entity.d_entity;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/7/5.
 */
public class TextEntity extends AbsDEntity {
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.TEXT_ITEM;
    }
}
