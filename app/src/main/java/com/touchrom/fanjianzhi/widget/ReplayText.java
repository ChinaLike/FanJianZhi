package com.touchrom.fanjianzhi.widget;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.StringUtil;
import com.lyy.ui.widget.CircleImageView;
import com.lyy.ui.widget.IconText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;
import com.touchrom.fanjianzhi.module.ArtModule;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/14.
 */
public class ReplayText extends RelativeLayout implements View.OnClickListener {
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
    CommentEntity.ChildCommentEntity mEntity;
    @InjectView(R.id.replay_line)
    View line;

    public ReplayText(Context context, CommentEntity.ChildCommentEntity entity) {
        super(context);
        init(context);
        mEntity = entity;
        setContent(mEntity);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_art_replay, this, true);
        ButterKnife.inject(this, view);
    }

    private void setContent(CommentEntity.ChildCommentEntity entity) {
        if (!TextUtils.isEmpty(entity.getByReplayUserName())) {
//            createSpan(content, entity.getChildContent(), entity.getChildNikeName(), entity.getByReplayUserId());
            String str = "回复" + entity.getByReplayUserName() + "：" + entity.getChildContent();
            content.setText(StringUtil.highLightStr(str, entity.getByReplayUserName(), Color.parseColor("#a3b0be")));
        } else {
            content.setText(entity.getChildContent());
        }
        nikeName.setText(entity.getChildNikeName());
        time.setText(entity.getChildTime());
        leave.setText(entity.getChildGrade());
        star.setText(entity.getChildStarNum() + "");
        star.setLeftDrawable(entity.isStared() ? R.mipmap.icon_stared : R.mipmap.icon_star);
        ImgHelp.setImg(getContext(), entity.getChildImgUrl(), img);
        star.setOnClickListener(this);
        nikeName.setOnClickListener(this);
    }

    public CommentEntity.ChildCommentEntity getEntity() {
        return mEntity;
    }

    public void setLineGone() {
        line.setVisibility(GONE);
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
    public void onClick(View v) {
        ArtModule module = new ArtModule(getContext());
        int id = v.getId();
        if (id == R.id.star) {
            module.star(mEntity.getChildCommentId(), 1, new StarResponse());
        }
    }

    /**
     * 点赞回调
     */
    private class StarResponse extends HttpUtil.AbsResponse {

        @Override
        public void onResponse(String data) {
            super.onResponse(data);
            boolean success = false;
            String str = "点赞失败";
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
                    int num = Integer.valueOf(star.getText().toString()) + 1;
                    mEntity.setChildStarNum(num);
                    mEntity.setStared(true);
                    star.setText(num + "");
                    star.setLeftDrawable(getContext().getResources().getDrawable(R.mipmap.icon_stared));
                }
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

}
