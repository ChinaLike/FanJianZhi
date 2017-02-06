package com.touchrom.fanjianzhi.adapter.delegate.msg_center;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.msg.MsgCenterEntity;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/15.
 */
public class SysMsgDelegate extends BaseMsgDelegate<MsgCenterEntity, SysMsgDelegate.SysMsgHolder> {

    public SysMsgDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public SysMsgHolder createHolder(View convertView) {
        return new SysMsgHolder(convertView);
    }

    @Override
    public void bindData(int position, SysMsgHolder holder, MsgCenterEntity item) {
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getDetail());
        holder.time.setText(item.getTime());
        holder.cb.setVisibility(showDelCb ? View.VISIBLE : View.GONE);
        CbListener cbl = (CbListener) holder.cb.getTag(holder.cb.getId());
        if (cbl == null) {
            cbl = new CbListener();
            holder.cb.setTag(holder.cb.getId(), cbl);
        }
        cbl.setPosition(position);
        holder.cb.setChecked(mCheck.get(position, false));
        holder.cb.setOnCheckedChangeListener(cbl);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_msg_sys;
    }

    class SysMsgHolder extends AbsRVHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.cb)
        CheckBox cb;

        public SysMsgHolder(View itemView) {
            super(itemView);
        }
    }
}
