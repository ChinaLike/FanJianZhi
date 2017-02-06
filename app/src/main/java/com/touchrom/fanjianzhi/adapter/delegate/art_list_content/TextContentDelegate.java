package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;
import com.touchrom.fanjianzhi.widget.ItemBottomBar;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 * 图文item
 */
public class TextContentDelegate extends BaseArtDelegate<MainContentListEntity, TextContentDelegate.ImgContentHolder> {

    public TextContentDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public ImgContentHolder createHolder(View convertView) {
        return new ImgContentHolder(convertView);
    }

    @Override
    public void bindData(int position, ImgContentHolder holder, MainContentListEntity item) {
        ImgTextEntity entity = item.getTextContent();
        CharSequence title = entity.getTitle();
        if (!TextUtils.isEmpty(mKey)) {
            title = tintKey(title.toString(), mKey);
        }
        holder.title.setText(title);
        holder.detail.setText(entity.getDetail());
        holder.nikeName.setText(entity.getNikeName());

        BottomBarHelp.handleBottomBar(holder.bottomBar, entity);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_content_text;
    }

    class ImgContentHolder extends AbsRVHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.detail)
        TextView detail;
        @InjectView(R.id.bottom_bar)
        ItemBottomBar bottomBar;
        @InjectView(R.id.login)
        TextView nikeName;

        public ImgContentHolder(View itemView) {
            super(itemView);
        }
    }
}
