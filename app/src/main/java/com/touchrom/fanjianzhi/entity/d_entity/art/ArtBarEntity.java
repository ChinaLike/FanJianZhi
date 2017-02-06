package com.touchrom.fanjianzhi.entity.d_entity.art;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.touchrom.fanjianzhi.config.Constance;

/**
 * Created by lyy on 2016/6/29.
 */
public class ArtBarEntity extends AbsDEntity{

    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getAbsType() {
        return Constance.ADAPTER.ART_BAR;
    }
}
