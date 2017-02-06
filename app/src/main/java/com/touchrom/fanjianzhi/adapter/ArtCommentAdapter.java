package com.touchrom.fanjianzhi.adapter;

import android.content.Context;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.touchrom.fanjianzhi.adapter.delegate.common.CommentDelegate;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;

import java.util.List;

/**
 * Created by lyy on 2016/6/29.
 */
public class ArtCommentAdapter extends AbsRvDAdapter<AbsDEntity> {
    public ArtCommentAdapter(Context context, List<AbsDEntity> data) {
        super(context, data);
        mManager.addDelegate(new CommentDelegate(context, this, Constance.ADAPTER.COMMENT));
    }

    public void updateReplay(int position, CommentEntity entity) {
        ((CommentDelegate) mManager.getDelegate(Constance.ADAPTER.COMMENT)).updateReplay(position, entity);
    }

    public void setCommentClickListener(CommentDelegate.CommentClickListener listener) {
        ((CommentDelegate) mManager.getDelegate(Constance.ADAPTER.COMMENT)).setCommentClickListener(listener);
    }
}
