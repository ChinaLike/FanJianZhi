package com.touchrom.fanjianzhi.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.listview.AbsLvAdapter;
import com.arialyy.absadapter.listview.AbsLvHolder;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.lyy.ui.widget.CircleImageView;
import com.lyy.ui.widget.IconText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.module.ArtModule;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/14.
 * 回复适配器
 */
public class ArtReplayAdapter extends AbsLvAdapter<CommentEntity.ChildCommentEntity, ArtReplayAdapter.ReplayHolder> {

    public ArtReplayAdapter(Context context, List<CommentEntity.ChildCommentEntity> data) {
        super(context, data);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.item_art_replay;
    }

    @Override
    public void bindData(int position, ReplayHolder holder, CommentEntity.ChildCommentEntity item) {
        ImgHelp.setImg(getContext(), item.getChildImgUrl(), holder.img);
        holder.nikeName.setText(item.getChildNikeName());
        holder.time.setText(item.getChildTime());
        holder.leave.setText(item.getChildGrade());
        holder.star.setText(item.getChildStarNum() + "");
        if (!TextUtils.isEmpty(item.getByReplayUserName())) {
            createSpan(holder.content, item.getChildContent(), item.getChildNikeName(), item.getByReplayUserId());
        } else {
            holder.content.setText(item.getChildContent());
        }

        ClickListener listener = (ClickListener) holder.nikeName.getTag(holder.nikeName.getId());
        if (listener == null) {
            listener = new ClickListener();
            holder.nikeName.setTag(holder.nikeName.getId(), listener);
        }
        listener.setData(holder, item);
        holder.nikeName.setOnClickListener(listener);
        holder.star.setOnClickListener(listener);
    }

    private void createSpan(TextView text, String content, String nikeName, int replayId) {
        String str = "回复 " + nikeName + "：" + content;
        SpannableString sps = (SpannableString) text.getTag(text.getId());
        if (sps == null && !TextUtils.isEmpty(nikeName)) {
            sps = new SpannableString(str);
            int index = str.indexOf(nikeName);
            sps.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), index, index + nikeName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new CustomClickSpan(replayId), index, index + nikeName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setLinkTextColor(createColor());
            text.setHighlightColor(Color.TRANSPARENT);
            text.setMovementMethod(LinkMovementMethod.getInstance());
            text.setTag(text.getId(), sps);
        }
        text.setText(sps);
    }

    private ColorStateList createColor() {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        states[0] = new int[]{android.R.attr.state_pressed};
        colors[0] = Color.parseColor("#bbbbbb");
        states[1] = new int[]{};
        colors[1] = Color.parseColor("#a3b0be");
        return new ColorStateList(states, colors);
    }

    @Override
    public ReplayHolder getViewHolder(View convertView) {
        return new ReplayHolder(convertView);
    }

    /**
     * 点赞回调
     */
    private class StarResponse extends HttpUtil.AbsResponse {

        ReplayHolder holder;
        CommentEntity.ChildCommentEntity entity;

        public StarResponse(ReplayHolder holder, CommentEntity.ChildCommentEntity entity) {
            this.holder = holder;
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
                    int num = Integer.valueOf(holder.star.getText().toString()) + 1;
                    entity.setChildStarNum(num);
                    holder.star.setText(num + "");
                    holder.star.setLeftDrawable(getContext().getResources().getDrawable(R.mipmap.ic_launcher));
                } else {
                    T.showShort(getContext(), "点赞失败");
                }
            }
        }
    }

    class ClickListener implements View.OnClickListener {
        private ReplayHolder holder;
        private CommentEntity.ChildCommentEntity entity;

        public void setData(ReplayHolder holder, CommentEntity.ChildCommentEntity entity) {
            this.holder = holder;
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            ArtModule module = new ArtModule(getContext());
            int id = v.getId();
            if (id == R.id.star) {
                module.star(entity.getChildCommentId(), 1, new StarResponse(holder, entity));
            }
        }
    }

    class CustomClickSpan extends ClickableSpan {
        int id;

        CustomClickSpan(int id) {
            this.id = id;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {

        }
    }

    class ReplayHolder extends AbsLvHolder {
        @InjectView(R.id.img)
        CircleImageView img;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.leave)
        TextView leave;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.star)
        IconText star;
        @InjectView(R.id.content)
        TextView content;

        public ReplayHolder(View view) {
            super(view);
        }
    }
}
