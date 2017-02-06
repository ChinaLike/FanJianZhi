package com.touchrom.fanjianzhi.adapter.delegate;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.BannerDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.BaseArtDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.ChannelDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.ImgContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.ImgTextContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.TextContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.VideoContentDelegate;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;

import java.util.List;

/**
 * Created by lyy on 2016/6/6.
 * 首页内容Adapter
 */
public class MainArtListAdapter extends AbsRvDAdapter<MainContentListEntity> {
    public MainArtListAdapter(Context context, FragmentManager fm, List<MainContentListEntity> data) {
        super(context, data);
        mManager.addDelegate(new BannerDelegate(context, this, Constance.ADAPTER.BANNER_ITEM, fm));
        mManager.addDelegate(new ImgContentDelegate(context, this, Constance.ADAPTER.IMG_CONTENT_ITEM));
        mManager.addDelegate(new TextContentDelegate(context, this, Constance.ADAPTER.TEXT_CONTENT_ITEM));
        mManager.addDelegate(new ImgTextContentDelegate(context, this, Constance.ADAPTER.IMG_TEXT_CONTENT_ITEM));
        mManager.addDelegate(new ChannelDelegate(context, this, Constance.ADAPTER.CHANNEL_CONTENT_ITEM));
        mManager.addDelegate(new VideoContentDelegate(context, this, Constance.ADAPTER.VIDEO_CONTENT_ITEM));
    }

    public void setKey(String key) {
        BaseArtDelegate imgDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.IMG_CONTENT_ITEM);
        BaseArtDelegate textDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.TEXT_CONTENT_ITEM);
        BaseArtDelegate imgTextDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.IMG_TEXT_CONTENT_ITEM);
        BaseArtDelegate videoDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.VIDEO_CONTENT_ITEM);
        if (imgDelegate != null) {
            imgDelegate.setKey(key);
        }
        if (textDelegate != null) {
            textDelegate.setKey(key);
        }
        if (imgTextDelegate != null) {
            imgTextDelegate.setKey(key);
        }
        if (videoDelegate != null) {
            videoDelegate.setKey(key);
        }
//        notifyDataSetChanged();
    }

    public void updateEntity(){
        BaseArtDelegate imgDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.IMG_CONTENT_ITEM);
        BaseArtDelegate textDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.TEXT_CONTENT_ITEM);
        BaseArtDelegate imgTextDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.IMG_TEXT_CONTENT_ITEM);
        BaseArtDelegate videoDelegate = (BaseArtDelegate) mManager.getDelegate(Constance.ADAPTER.VIDEO_CONTENT_ITEM);
        if (imgDelegate != null) {
            imgDelegate.updateSetting();
        }
        if (textDelegate != null) {
            textDelegate.updateSetting();
        }
        if (imgTextDelegate != null) {
            imgTextDelegate.updateSetting();
        }
        if (videoDelegate != null) {
            videoDelegate.updateSetting();
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(getContext()).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
    }

}
