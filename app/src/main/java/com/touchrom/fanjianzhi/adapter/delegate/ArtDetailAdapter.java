package com.touchrom.fanjianzhi.adapter.delegate;

import android.content.Context;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.touchrom.fanjianzhi.adapter.delegate.art_detail.ArtContentBarDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_detail.ArtContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.ImgContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.ImgTextContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.TextContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.VideoContentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.common.CommentDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.common.TextItemDelegate;
import com.touchrom.fanjianzhi.config.Constance;

import java.util.List;

/**
 * Created by lyy on 2016/6/7.
 */
public class ArtDetailAdapter extends AbsRvDAdapter<AbsDEntity> {
    public ArtDetailAdapter(Context context, List<AbsDEntity> data) {
        super(context, data);
        mManager.addDelegate(new ArtContentDelegate(context, this, Constance.ADAPTER.ART_CONTENT_ITEM));
        mManager.addDelegate(new ImgContentDelegate(context, this, Constance.ADAPTER.IMG_CONTENT_ITEM));
        mManager.addDelegate(new TextContentDelegate(context, this, Constance.ADAPTER.TEXT_CONTENT_ITEM));
        mManager.addDelegate(new ImgTextContentDelegate(context, this, Constance.ADAPTER.IMG_TEXT_CONTENT_ITEM));
        mManager.addDelegate(new VideoContentDelegate(context, this, Constance.ADAPTER.VIDEO_CONTENT_ITEM));
        mManager.addDelegate(new ArtContentBarDelegate(context, this, Constance.ADAPTER.ART_BAR));
        mManager.addDelegate(new CommentDelegate(context, this, Constance.ADAPTER.COMMENT));
        mManager.addDelegate(new TextItemDelegate(context, this, Constance.ADAPTER.TEXT_ITEM));
    }

    public void setRichTextSize(float size) {
        ((ArtContentDelegate) mManager.getDelegate(Constance.ADAPTER.ART_CONTENT_ITEM)).setRicHTextSize(size);
        notifyItemChanged(0);
    }

}
