package com.touchrom.fanjianzhi.test;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.recycler_view.AbsRVAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/2.
 */
public class TestAdapter extends AbsRVAdapter<String, TestAdapter.RVHolder> {

    public TestAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    protected RVHolder getViewHolder(View convertView, int viewType) {
        return new RVHolder(convertView);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.test_item_rv;
    }

    @Override
    protected void bindData(RVHolder holder, int position, String item) {
        holder.text.setText(item);
    }

    class RVHolder extends AbsRVHolder {
        @InjectView(R.id.text)
        TextView text;

        public RVHolder(View itemView) {
            super(itemView);
        }
    }
}
