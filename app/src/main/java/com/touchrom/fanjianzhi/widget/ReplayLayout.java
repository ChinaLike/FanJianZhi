package com.touchrom.fanjianzhi.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arialyy.frame.util.DensityUtils;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;

import java.util.List;

/**
 * Created by lyy on 2016/6/14.
 */
public class ReplayLayout extends LinearLayout implements View.OnClickListener {

    private List<CommentEntity.ChildCommentEntity> mLists;
    private OnItemClickListener mListener;
    private boolean showAllContent = false;
    private boolean isFirst = true;


    public interface OnItemClickListener {
        public void onItemClick(ReplayText text);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ReplayLayout(Context context) {
        this(context, null);
    }

    public ReplayLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
    }

    public void setContent(List<CommentEntity.ChildCommentEntity> lists) {
        mLists = lists;
        removeAllViews();
        int i = 0;
        if (lists.size() > 2 && isFirst) {
            showAllContent = true;
        }
        for (CommentEntity.ChildCommentEntity entity : lists) {
            if (showAllContent && i > 1) {  //显示2条
                addView(createMoreBt("点击显示剩余" + (lists.size() - 2) + "条回复"));
                return;
            }
            addView(createReplay(entity, i));
            i++;
        }
    }

    private ReplayText createReplay(CommentEntity.ChildCommentEntity entity, int position) {
        ReplayText replayText = new ReplayText(getContext(), entity);
        if (position == mLists.size() - 1) {
            replayText.setLineGone();
        }
        replayText.setOnClickListener(this);
        return replayText;
    }

    private View createMoreBt(String text) {
        int padding = DensityUtils.dp2px(8);
        TextView view = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        view.setPadding(padding, padding, padding, padding);
        view.setLayoutParams(params);
        view.setTextColor(createColor());
        view.setText(text);
        view.setTextSize(14);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllContent = false;
                isFirst = false;
                setContent(mLists);
            }
        });
        return view;
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
        if (mListener != null) {
            mListener.onItemClick((ReplayText) v);
        }
    }

}
