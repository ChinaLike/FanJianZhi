package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.bumptech.glide.Glide;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 * 频道item
 */
public class ChannelDelegate extends BaseArtDelegate<MainContentListEntity, ChannelDelegate.ChannelHolder> {


    public ChannelDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public ChannelHolder createHolder(View convertView) {
        return new ChannelHolder(convertView);
    }

    @Override
    public void bindData(int position, ChannelHolder holder, MainContentListEntity item) {
        ImgTextEntity entity = item.getChannelContent();
        if (mSettingEntity.isShowImg()) {
            Glide.with(getContext()).load(entity.getImgUrl()).into(holder.img);
        }
        holder.title.setText(entity.getDetail());
        holder.num.setVisibility(entity.getNum() == 0 ? View.GONE : View.VISIBLE);
        holder.num.setText(entity.getNum() + "");
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_content_channel;
    }

    class ChannelHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        CircleImageView img;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.num)
        TextView num;

        public ChannelHolder(View itemView) {
            super(itemView);
        }
    }
}
