package com.touchrom.fanjianzhi.entity;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.touchrom.fanjianzhi.entity.d_entity.BannerEntity;
import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;

import java.util.List;

/**
 * Created by lyy on 2016/6/6.
 */
public class MainContentListEntity extends AbsDEntity {
    BannerArray banner;
    ImgTextEntity imgContent;       //纯图
    ImgTextEntity textContent;      //纯文字
    ImgTextEntity imgTextContent;   //图文
    ImgTextEntity videoContent;     //视频
    ImgTextEntity channelContent;   //频道
    private int type = -1;

    @Override
    public int getAbsType() {
        if (type == -1) {
            if (imgContent != null) {
                type = imgContent.getType();
            } else if (textContent != null) {
                type = textContent.getType();
            } else if (imgTextContent != null) {
                type = imgTextContent.getType();
            } else if (videoContent != null) {
                type = videoContent.getType();
            } else if (channelContent != null) {
                type = channelContent.getType();
            } else if (banner != null) {
                type = banner.type;
            }
        }
        return type;
    }

    public int getType() {
        return type;
    }

    public BannerArray getBanner() {
        return banner;
    }

    public ImgTextEntity getImgContent() {
        return imgContent;
    }

    public ImgTextEntity getTextContent() {
        return textContent;
    }

    public ImgTextEntity getImgTextContent() {
        return imgTextContent;
    }

    public ImgTextEntity getVideoContent() {
        return videoContent;
    }

    public ImgTextEntity getChannelContent() {
        return channelContent;
    }

    public static class BannerArray extends AbsDEntity {
        int type;
        List<BannerEntity> content;

        public List<BannerEntity> getContent() {
            return content;
        }

        @Override
        public int getAbsType() {
            return type;
        }
    }
}
