package com.touchrom.fanjianzhi.adapter.delegate.msg_center;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.lyy.ui.widget.CircleImageView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.msg.ReplayEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/15.
 */
public class ReplayMsgDelegate extends BaseMsgDelegate<ReplayEntity, ReplayMsgDelegate.ReplayHolder> {

    public ReplayMsgDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public ReplayHolder createHolder(View convertView) {
        return new ReplayHolder(convertView);
    }

    @Override
    public void bindData(int position, ReplayHolder holder, ReplayEntity item) {
        ImgHelp.setImg(getContext(), item.getImgUrl(), holder.img);
        holder.userName.setText(item.getCmNikeName());
        holder.time.setText(item.getTime());
        holder.title.setText("原文：" + item.getArtTitle());
        holder.comment.setText(item.getMyNikeName() + "：" + item.getMyCmContent());
        holder.replay.setText(item.getCmContent());
        ReplayBtListener listener = (ReplayBtListener) holder.bt.getTag(holder.bt.getId());
        if (listener == null) {
            listener = new ReplayBtListener();
            holder.bt.setTag(holder.bt.getId(), listener);
        }
        holder.title.setOnClickListener(listener);
        if (showDelCb) {
            holder.bt.setVisibility(View.GONE);
            holder.cb.setVisibility(View.VISIBLE);
            CbListener cbl = (CbListener) holder.cb.getTag(holder.cb.getId());
            if (cbl == null) {
                cbl = new CbListener();
                holder.cb.setTag(holder.cb.getId(), cbl);
            }
            cbl.setPosition(position);
            holder.cb.setChecked(mCheck.get(position, false));
            holder.cb.setOnCheckedChangeListener(cbl);
        } else {
            listener.setReplayEntity(item);
            holder.bt.setOnClickListener(listener);
            holder.bt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_msg_replay;
    }

    class ReplayHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        CircleImageView img;
        @InjectView(R.id.user_name)
        TextView userName;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.replay)
        TextView replay;
        @InjectView(R.id.bt)
        ImageView bt;
        @InjectView(R.id.comment)
        TextView comment;
        @InjectView(R.id.cb)
        CheckBox cb;

        public ReplayHolder(View itemView) {
            super(itemView);
        }
    }
}
