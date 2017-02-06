package com.touchrom.fanjianzhi.adapter.delegate.art_detail;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.delegate.art_list_content.BaseArtDelegate;
import com.touchrom.fanjianzhi.entity.d_entity.art.ArtBarEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/29.
 */
public class ArtContentBarDelegate extends BaseArtDelegate<ArtBarEntity, ArtContentBarDelegate.ArtContentBarHolder> {

    public ArtContentBarDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public ArtContentBarHolder createHolder(View convertView) {
        return new ArtContentBarHolder(convertView);
    }

    @Override
    public void bindData(int position, ArtContentBarHolder holder, ArtBarEntity item) {
        holder.hint.setText(item.getTitle());
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_art_content_bar;
    }

    class ArtContentBarHolder extends AbsRVHolder {
        @InjectView(R.id.hint)
        TextView hint;

        public ArtContentBarHolder(View itemView) {
            super(itemView);
        }
    }
}
