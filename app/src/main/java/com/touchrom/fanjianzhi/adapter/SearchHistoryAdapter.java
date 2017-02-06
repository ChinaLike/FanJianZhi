package com.touchrom.fanjianzhi.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.listview.AbsLvAdapter;
import com.arialyy.absadapter.listview.AbsLvHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.entity.SearchAdapterEntity;
import com.touchrom.fanjianzhi.entity.sql.SearchHistoryEntity;
import com.touchrom.fanjianzhi.module.SettingModule;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/15.
 */
public class SearchHistoryAdapter extends AbsLvAdapter<SearchAdapterEntity, SearchHistoryAdapter.SearchAdapterHolder> {

    public SearchHistoryAdapter(Context context, List<SearchAdapterEntity> data) {
        super(context, data);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    protected int setLayoutId(int type) {
        if (type == 1) {
            return R.layout.item_search;
        } else if (type == 2) {
            return R.layout.item_search_clean;
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public void bindData(int position, SearchAdapterHolder holder, SearchAdapterEntity item) {
        holder.text.setText(item.getText());
        if (getItemViewType(position) == 2) {
            BtListener listener = (BtListener) holder.text.getTag(holder.text.getId());
            if (listener == null) {
                listener = new BtListener();
                holder.text.setTag(holder.text.getId(), listener);
            }
            holder.text.setOnClickListener(listener);
        }
        if (holder.img != null) {
            holder.img.setImageResource(item.getImg());
        }
    }

    private class BtListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            new SettingModule(getContext()).delSearchRecord();
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public SearchAdapterHolder getViewHolder(View convertView) {
        return new SearchAdapterHolder(convertView);
    }

    class SearchAdapterHolder extends AbsLvHolder {
        @InjectView(R.id.text)
        TextView text;
        ImageView img;

        public SearchAdapterHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
