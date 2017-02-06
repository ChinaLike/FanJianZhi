package com.touchrom.fanjianzhi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arialyy.absadapter.listview.AbsSimpleAdapter;
import com.arialyy.absadapter.listview.AbsSimpleViewHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityMsgCenterBinding;
import com.touchrom.fanjianzhi.entity.d_entity.msg.MsgCenterEntity;
import com.touchrom.fanjianzhi.module.MsgModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/15.
 * 消息中心
 */
public class MsgCenterActivity extends BaseActivity<ActivityMsgCenterBinding> {
    @InjectView(R.id.list)
    ListView mList;
    AbsSimpleAdapter<MsgCenterEntity> mAdapter;
    List<MsgCenterEntity> mData = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("消息");
        mAdapter = new AbsSimpleAdapter<MsgCenterEntity>(this, mData, R.layout.item_msg_center) {
            @Override
            public void convert(AbsSimpleViewHolder helper, MsgCenterEntity item) {
                helper.setImageResource(R.id.img, item.getImg());
                helper.getView(R.id.dot).setVisibility(item.isHasNewMsg() ? View.VISIBLE : View.GONE);
                helper.setText(R.id.time, item.getTime() + "");
                helper.setText(R.id.title, item.getTitle());
                helper.setText(R.id.detail, item.getDetail());
            }
        };
        mList.setAdapter(mAdapter);
        getModule(MsgModule.class).getMsgCenterData();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MsgCenterActivity.this, MsgActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra(Constance.KEY.TYPE, Constance.ADAPTER.MSG_REPLAY);
                        break;
                    case 1:
                        intent.putExtra(Constance.KEY.TYPE, Constance.ADAPTER.MSG_CALL);
                        break;
                    case 2:
                        intent.putExtra(Constance.KEY.TYPE, Constance.ADAPTER.MSG_SYS);
                        break;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_msg_center;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (result == ResultCode.SERVER.GET_MSG_CENTER_DATA) {
            String[] titles = new String[]{"收到回复", "@我的", "系统消息"};
            int[] imgs = new int[]{R.mipmap.icon_msg_comment, R.mipmap.icon_msg_call_me, R.mipmap.icon_msg_sys};
            List<MsgCenterEntity> list;
            if (obj != null) {
                list = (List<MsgCenterEntity>) obj;
                list.get(0).setTitle(titles[0]).setImg(imgs[0]);
                list.get(1).setTitle(titles[1]).setImg(imgs[1]);
                list.get(2).setTitle(titles[2]).setImg(imgs[2]);
            } else {
                String detail = "没有任何消息";
                list = new ArrayList<>();
                list.get(0).setTitle(titles[0]).setDetail(detail).setImg(imgs[0]);
                list.get(1).setTitle(titles[1]).setDetail(detail).setImg(imgs[1]);
                list.get(2).setTitle(titles[2]).setDetail(detail).setImg(imgs[2]);
            }
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}
