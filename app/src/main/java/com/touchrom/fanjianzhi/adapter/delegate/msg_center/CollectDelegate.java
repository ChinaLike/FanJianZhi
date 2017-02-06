package com.touchrom.fanjianzhi.adapter.delegate.msg_center;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.d_entity.msg.CollectEntity;
import com.touchrom.fanjianzhi.help.ImgHelp;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/16.
 * 收藏
 */
public class CollectDelegate extends BaseMsgDelegate<CollectEntity, CollectDelegate.CollectHolder> {

    public CollectDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    @Override
    public CollectHolder createHolder(View convertView) {
        return new CollectHolder(convertView);
    }

    @Override
    public void bindData(int position, CollectHolder holder, CollectEntity item) {
        ImgHelp.setImg(getContext(), item.getImgUrl(), holder.img);
        holder.title.setText(item.getArtTitle());
        holder.nikeName.setText(item.getNikeName());
        if (showDelCb) {
            holder.cb.setVisibility(View.VISIBLE);
            CbListener cbl = (CbListener) holder.cb.getTag(holder.cb.getId());
            if (cbl == null) {
                cbl = new CbListener();
                holder.cb.setTag(holder.cb.getId(), cbl);
            }
            cbl.setPosition(position);
            holder.cb.setChecked(mCheck.get(position, false));
            holder.cb.setOnCheckedChangeListener(cbl);
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_msg_collect;
    }

    class CollectHolder extends AbsRVHolder{
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.nike_name)
        TextView nikeName;
        @InjectView(R.id.cb)
        CheckBox cb;
        public CollectHolder(View itemView) {
            super(itemView);
        }
    }
}
