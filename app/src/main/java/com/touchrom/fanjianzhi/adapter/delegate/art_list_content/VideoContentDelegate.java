package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.widget.ItemBottomBar;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 * 视频Item
 */
public class VideoContentDelegate extends BaseArtDelegate<MainContentListEntity, VideoContentDelegate.ImgContentHolder> {

    public VideoContentDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public ImgContentHolder createHolder(View convertView) {
        return new ImgContentHolder(convertView);
    }

    @Override
    public void bindData(int position, ImgContentHolder holder, MainContentListEntity item) {
        ImgTextEntity entity = item.getVideoContent();
        if (mSettingEntity.isShowImg()) {
            ImgHelp.setImg(getContext(), entity.getImgUrl(), R.mipmap.icon_def_icon, holder.img);
        } else {
            Glide.with(getContext()).load(R.mipmap.icon_def_icon).into(holder.img);
        }
        CharSequence title = entity.getTitle();
        if (!TextUtils.isEmpty(mKey)) {
            title = tintKey(title.toString(), mKey);
        }
        holder.title.setText(title);
        holder.nikeName.setText(entity.getNikeName());
        holder.artType.setVisibility(TextUtils.isEmpty(entity.getTagName()) ? View.GONE : View.VISIBLE);
        holder.artType.setText(entity.getTagName());

        BottomBarHelp.handleBottomBar(holder.bottomBar, entity);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_content_img_text;
    }

    class ImgContentHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.login)
        TextView nikeName;
        @InjectView(R.id.bottom_bar)
        ItemBottomBar bottomBar;
        @InjectView(R.id.art_type)
        TextView artType;

        public ImgContentHolder(View itemView) {
            super(itemView);
        }
    }
}
