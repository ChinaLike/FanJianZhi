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
import com.touchrom.fanjianzhi.entity.d_entity.msg.CallMeEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/15.
 */
public class CallMeMsgDelegate extends BaseMsgDelegate<CallMeEntity, CallMeMsgDelegate.CallMeHolder> {

    public CallMeMsgDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public CallMeHolder createHolder(View convertView) {
        return new CallMeHolder(convertView);
    }

    @Override
    public void bindData(int position, CallMeHolder holder, CallMeEntity item) {
        ImgHelp.setImg(getContext(), item.getImgUrl(), holder.img);
        holder.nikeName.setText(item.getNikeName());
        holder.time.setText(item.getTime());
        holder.title.setText("原文：" + item.getArtTitle());
        holder.content.setText(item.getContent());
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
            listener.setCallMeEntity(item);
            holder.bt.setOnClickListener(listener);
            holder.bt.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int setLayoutId() {
        return R.layout.item_msg_call_me;
    }

    class CallMeHolder extends AbsRVHolder {
        @InjectView(R.id.img)
        CircleImageView img;
        @InjectView(R.id.user_name)
        TextView nikeName;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.bt)
        ImageView bt;
        @InjectView(R.id.cb)
        CheckBox cb;

        public CallMeHolder(View itemView) {
            super(itemView);
        }
    }
}
