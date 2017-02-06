package com.touchrom.fanjianzhi.adapter.delegate.art_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.SharePreUtil;
import com.arialyy.frame.util.show.T;
import com.bumptech.glide.Glide;
import com.lyy.ui.rich_text.GlideImageGetter;
import com.lyy.ui.rich_text.RichText;
import com.lyy.ui.widget.IconText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.LoginActivity;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.dialog.ComplainDialog;
import com.touchrom.fanjianzhi.dialog.ImgBrowseDialog;
import com.touchrom.fanjianzhi.entity.WebEntity;
import com.touchrom.fanjianzhi.entity.d_entity.art.ArtContentEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.module.ArtModule;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/7.
 * 文章内容
 */
public class ArtContentDelegate extends BaseRvDelegate<ArtContentEntity, ArtContentDelegate.ArtContentHolder> {

    private float textSize = -1;

    public ArtContentDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
        textSize = SharePreUtil.getInt(Constance.APP.NAME, getContext(), "fontSize");
    }

    @Override
    public ArtContentHolder createHolder(View convertView) {
        return new ArtContentHolder(convertView);
    }

    @Override
    public void bindData(int position, ArtContentHolder holder, ArtContentEntity item) {
        holder.title.setText(item.getTitle());
        holder.scanNum.setText("浏览：" + item.getScanNum());
        holder.classify.setText("分类：" + item.getCategory());
        holder.time.setText(item.getTime());
        ImgHelp.setImg(getContext(), item.getImgUrl(), holder.img);
        holder.nikeName.setText(item.getAuthorName());
        holder.sign.setText(item.getSign());
        holder.follow.setEnabled(!item.isFollow());
        if (textSize > 0) {
            holder.content.setTextSize(textSize);
        }
        holder.follow.setText(item.isFollow() ? "已关注" : "+ 关注");
        FollowBtListener listener = (FollowBtListener) holder.follow.getTag(holder.follow.getId());
        if (listener == null) {
            listener = new FollowBtListener();
            holder.follow.setTag(holder.follow.getId(), listener);
        }
        listener.setEntity(holder, position, item);
        holder.star.setText(item.getStarNum() + "");
        holder.star.setLeftDrawable(item.isStared() ? R.mipmap.icon_stared : R.mipmap.icon_star);
        holder.star.setOnClickListener(listener);
        holder.unStar.setText(item.getUnStarNum() + "");
        holder.unStar.setLeftDrawable(item.isUnStared() ? R.mipmap.icon_unstared : R.mipmap.icon_unstar);
        holder.unStar.setOnClickListener(listener);
        holder.follow.setOnClickListener(listener);
        holder.complaints.setOnClickListener(listener);
        holder.content.setShowImg(mSettingEntity.isShowImg());
        if (!TextUtils.isEmpty(item.getContent())) {
            holder.content.setRichText(item.getContent(), mFixUrlListener);
        }
        holder.content.setOnImageClickListener(mImgClickListener);
        holder.content.setOnURLClickListener(mUrlClickListener);
//        holder.content.setFixUrlListener(mFixUrlListener);
    }

    RichText.OnImageClickListener mImgClickListener = new RichText.OnImageClickListener() {
        @Override
        public void imageClicked(List<String> imageUrls, int position) {
            ImgBrowseDialog dialog = new ImgBrowseDialog(imageUrls, position);
            FragmentManager fm = getFm();
            if (fm != null) {
                dialog.show(fm, "imgbrowdialog");
            }
        }
    };

    RichText.OnURLClickListener mUrlClickListener = new RichText.OnURLClickListener() {
        @Override
        public boolean urlClicked(String url) {
            turnWeb(url);
            return true;
        }
    };

    GlideImageGetter.OnFixUrlCallback mFixUrlListener = new GlideImageGetter.OnFixUrlCallback() {
        @Override
        public String fixUrl(String url) {
            boolean isGif = FileUtil.getFileExtensionName(url).equals("gif");
            String fixUrl;
            if (isGif) {
                fixUrl = "http://ww1.rs.fanjian.net/ig.php?type=giff&img=" + url;
            } else {
                String start = "http://ww1.rs.fanjian.net/i.php?img=";
                String end = "&type=app_avatar";
                fixUrl = start + url + end;
            }
            return fixUrl;
        }
    };


    public void setRicHTextSize(float size) {
        textSize = size;
    }

    private void turnWeb(String url) {
//        WebEntity entity = new WebEntity(url);
//        TurnHelp.getINSTANCE().turnVideoWeb(getContext(), entity);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            TurnHelp.getINSTANCE().turnSysWeb(getContext(), url);
        } else {
            WebEntity entity = new WebEntity(url);
            TurnHelp.getINSTANCE().turnVideoWeb(getContext(), entity);
        }
    }

    private class FollowBtListener implements View.OnClickListener {
        ArtContentEntity entity;
        int position;
        ArtContentHolder holder;

        public void setEntity(ArtContentHolder holder, int position, ArtContentEntity entity) {
            this.entity = entity;
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            ArtModule module = new ArtModule(getContext());
            if (id == R.id.follow) {
                if (!AppManager.getInstance().isLogin()) {
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    module.follow(entity.getAuthorId(), new FollowResponse(entity));
                }
            } else if (id == R.id.complaints) {
                FragmentManager fm = getFm();
                if (fm != null) {
                    ComplainDialog dialog = new ComplainDialog(entity.getId());
                    dialog.show(fm, "complainDialog");
                }
            } else if (id == R.id.star) {
                module.star(entity.getId(), 2, new StarResponse(holder, position, entity));
            } else if (id == R.id.un_star) {
                module.unStar(entity.getId(), new UnStarResponse(holder, position, entity));
            }
        }
    }

    /**
     * 关注回调
     */
    private class FollowResponse extends HttpUtil.AbsResponse {
        ArtContentEntity entity;

        public FollowResponse(ArtContentEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onResponse(String data) {
            super.onResponse(data);
            boolean success = false;
            try {
                JSONObject obj = new JSONObject(data);
                if (obj.getInt("code") == 200) {
                    JSONObject content = obj.getJSONObject(ServiceUtil.DATA_KEY);
                    success = content.getBoolean("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (success) {
                    entity.setFollow(true);
                    getAdapter().notifyDataSetChanged();
                } else {
                    T.showShort(getContext(), "关注失败");
                }
            }
        }

    }

    /**
     * 点赞回调
     */
    private class StarResponse extends HttpUtil.AbsResponse {

        ArtContentEntity entity;
        int position;
        ArtContentHolder holder;

        public StarResponse(ArtContentHolder holder, int position, ArtContentEntity entity) {
            this.entity = entity;
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onResponse(String data) {
            super.onResponse(data);
            boolean success = false;
            try {
                JSONObject obj = new JSONObject(data);
                if (obj.getInt("code") == 200) {
                    JSONObject content = obj.getJSONObject(ServiceUtil.DATA_KEY);
                    success = content.getBoolean("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (success) {
                    int num = entity.getStarNum() + 1;
                    entity.setStarNum(num);
                    entity.setStared(true);
//                    getAdapter().notifyItemChanged(position);
                    holder.star.setLeftDrawable(R.mipmap.icon_stared);
                    holder.star.setText(entity.getStarNum() + "");
                }
            }
        }
    }

    /**
     * 点踩回调
     */
    private class UnStarResponse extends HttpUtil.AbsResponse {
        ArtContentEntity entity;
        int position;
        ArtContentHolder holder;

        public UnStarResponse(ArtContentHolder holder, int position, ArtContentEntity entity) {
            this.entity = entity;
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onResponse(String data) {
            super.onResponse(data);
            boolean success = false;
            try {
                JSONObject obj = new JSONObject(data);
                if (obj.getInt("code") == 200) {
                    JSONObject content = obj.getJSONObject(ServiceUtil.DATA_KEY);
                    success = content.getBoolean("result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (success) {
                    int num = entity.getUnStarNum() + 1;
                    entity.setUnStarNum(num);
                    entity.setUnStared(true);
//                    getAdapter().notifyDataSetChanged();
//                    getAdapter().notifyItemChanged(position);
                    holder.unStar.setText(entity.getUnStarNum() + "");
                    holder.unStar.setLeftDrawable(R.mipmap.icon_unstared);
                }
            }
        }
    }


    @Override
    public int setLayoutId() {
        return R.layout.item_art_content;
    }

    class ArtContentHolder extends AbsRVHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.content)
        RichText content;
        @InjectView(R.id.scan_num)
        TextView scanNum;
        @InjectView(R.id.classify)
        TextView classify;
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.sign)
        TextView sign;
        @InjectView(R.id.follow)
        Button follow;
        @InjectView(R.id.complaints)
        TextView complaints;
        @InjectView(R.id.star)
        IconText star;
        @InjectView(R.id.un_star)
        IconText unStar;

        public ArtContentHolder(View itemView) {
            super(itemView);
        }
    }
}
