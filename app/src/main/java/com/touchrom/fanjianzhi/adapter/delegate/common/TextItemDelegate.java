package com.touchrom.fanjianzhi.adapter.delegate.common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDelegation;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.TextEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/7/5.
 */
public class TextItemDelegate extends AbsRvDelegation<TextEntity, TextItemDelegate.TextItemHolder> {

    public TextItemDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public TextItemHolder createHolder(View convertView) {
        return new TextItemHolder(convertView);
    }

    @Override
    public void bindData(int position, TextItemHolder holder, TextEntity item) {

    }

    @Override
    public int setLayoutId() {
        return R.layout.item_all_comment;
    }

    class TextItemHolder extends AbsRVHolder {
        @InjectView(R.id.text)
        TextView text;

        public TextItemHolder(View itemView) {
            super(itemView);
        }
    }
}
