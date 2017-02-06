package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDelegation;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.DensityUtils;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.L;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.transcode.BitmapToGlideDrawableTranscoder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;
import com.touchrom.fanjianzhi.widget.ItemBottomBar;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 * 纯图item
 */
public class ImgContentDelegate extends BaseArtDelegate<MainContentListEntity, ImgContentDelegate.ImgContentHolder> {

    private int mW, mH;

    public ImgContentDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
        int[] wh = AndroidUtils.getScreenParams(context);
        mW = wh[0] - DensityUtils.dp2px(32);
        mH = mW * 4 / 5;
    }

    @Override
    public ImgContentHolder createHolder(View convertView) {
        return new ImgContentHolder(convertView);
    }

    @Override
    public void bindData(int position, ImgContentHolder holder, MainContentListEntity item) {
        ImgTextEntity entity = item.getImgContent();
        String url = entity.getImgUrl();
        RequestManager manager = Glide.with(getContext());
        if (mSettingEntity.isShowImg()) {
            if (FileUtil.getFileExtensionName(url).equals("gif")) {
                if (entity.isPlayGif()) {
                    manager.load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE) //加载gif不能用all或者result
                            .placeholder(R.mipmap.icon_def_icon)
                            .override(mW, mH).crossFade().into(holder.img);
                } else {
//                    type.asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .placeholder(R.mipmap.icon_def_icon)
//                            .override(mW, mH).into(holder.img);
                    String bmGifUrl = "http://ww1.rs.fanjian.net/ig.php?type=giff&img=" + url;
                    manager.load(bmGifUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.icon_def_icon)
                            .override(mW, mH).crossFade().into(holder.img);
                }
                //显示播放按钮
                if (entity.isPlayGif()) {
                    holder.fl.setVisibility(View.GONE);
                } else {
                    PlayListener listener = (PlayListener) holder.play.getTag(holder.play.getId());
                    holder.fl.setVisibility(View.VISIBLE);
                    if (listener == null || listener.hashCode != entity.hashCode()) {
                        listener = new PlayListener(position, entity);
                        holder.play.setTag(holder.play.getId(), listener);
                        holder.play.setOnClickListener(listener);
                    }
                }
            } else {
                holder.fl.setVisibility(View.GONE);
                manager.load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.icon_def_icon)
                        .override(mW, mH).crossFade().into(holder.img);
            }
        } else {
            Glide.with(getContext()).load(R.mipmap.icon_def_banner).into(holder.img);
        }

        holder.artType.setVisibility(TextUtils.isEmpty(entity.getTagName()) ? View.GONE : View.VISIBLE);
        holder.artType.setText(entity.getTagName());
        CharSequence title = entity.getTitle();
        if (!TextUtils.isEmpty(mKey)) {
            title = tintKey(title.toString(), mKey);
        }
        holder.title.setText(title);
        holder.nikeName.setText(entity.getNikeName());
        BottomBarHelp.handleBottomBar(holder.bottomBar, entity);
    }

    private class PlayListener implements View.OnClickListener {
        ImgTextEntity entity;
        int position;
        int hashCode;

        public PlayListener(int position, ImgTextEntity entity) {
            this.position = position;
            this.entity = entity;
            hashCode = entity.hashCode();
        }

        @Override
        public void onClick(View v) {
            entity.setPlayGif(true);
//            getAdapter().notifyItemChanged(position + 1);
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_content_img;
    }

    class ImgContentHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.bottom_bar)
        ItemBottomBar bottomBar;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.play)
        ImageView play;
        @InjectView(R.id.art_type)
        TextView artType;
        @InjectView(R.id.fl)
        FrameLayout fl;

        public ImgContentHolder(View itemView) {
            super(itemView);
        }
    }
}
