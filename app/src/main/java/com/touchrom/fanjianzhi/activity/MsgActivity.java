package com.touchrom.fanjianzhi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.help.RvItemClickSupport;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.pulltorefresh.xrecyclerview.XRecyclerView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.delegate.MsgAdapter;
import com.touchrom.fanjianzhi.adapter.delegate.msg_center.BaseMsgDelegate;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivityMsgBinding;
import com.touchrom.fanjianzhi.entity.d_entity.msg.CollectEntity;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.module.MsgModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/15.
 * 消息管理
 */
public class MsgActivity extends BaseActivity<ActivityMsgBinding> implements XRecyclerView.LoadingListener,
        BaseMsgDelegate.OnCheckListener {
    @InjectView(R.id.list)
    XRecyclerView mList;
    @InjectView(R.id.del)
    TextView mDel;
    @InjectView(R.id.hint)
    TextView mHint;
    boolean isRefresh = true;
    int mPage = 1;
    List<AbsDEntity> mData = new ArrayList<>();
    MsgAdapter mAdapter;
    int mType = -1;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mType = getIntent().getIntExtra(Constance.KEY.TYPE, -1);
        if (mType == -1) {
            L.e(TAG, "请输入正确的type");
            finish();
            return;
        }
        showTempView(ITempView.LOADING);
        mAdapter = new MsgAdapter(this, mData);
        mAdapter.setOnCheckListener(mType, this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mList.setLayoutManager(lm);
        mList.setAdapter(mAdapter);
        mList.setLoadingListener(this);
        String title = "";
        switch (mType) {
            case Constance.ADAPTER.MSG_CALL:
                title = "@我的";
                break;
            case Constance.ADAPTER.MSG_REPLAY:
                title = "收到的回复";
                break;
            case Constance.ADAPTER.MSG_SYS:
                title = "系统消息";
                break;
            case Constance.ADAPTER.MSG_COLLECT:
                title = "收藏";
                mHint.setText("没有任何收藏~~");
                break;
        }
        setTitle(title);
        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.del(mType);
                mDel.setBackgroundColor(Color.TRANSPARENT);
                mDel.setVisibility(View.INVISIBLE);
                if (mData.size() == 0) {
                    mList.setVisibility(View.GONE);
                    mHint.setVisibility(View.VISIBLE);
                }
                mBar.getRightBt().setVisibility(mData.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        getModule(MsgModule.class).getData(mType, mPage);
        mBar.getRightBt().setImageResource(R.mipmap.icon_del);
        if (mData.size() != 0) {
            mBar.getRightBt().setVisibility(View.VISIBLE);
        }

//        RvItemClickSupport.addTo(mList).setOnItemClickListener(new RvItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerView parent, View view, int position, long id) {
//                switch (mType) {
//                    case Constance.ADAPTER.MSG_CALL:
//                        break;
//                    case Constance.ADAPTER.MSG_REPLAY:
//                        break;
//                    case Constance.ADAPTER.MSG_SYS:
//                        break;
//                    case Constance.ADAPTER.MSG_COLLECT:
//                        CollectEntity collectEntity = (CollectEntity) mData.get(position - 1);
//                        TurnHelp.getINSTANCE().turnArt(MsgActivity.this, collectEntity.getId());
//                        break;
//                }
//            }
//        });
        mAdapter.setOnItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                switch (mType) {
                    case Constance.ADAPTER.MSG_CALL:
                        break;
                    case Constance.ADAPTER.MSG_REPLAY:
                        break;
                    case Constance.ADAPTER.MSG_SYS:
                        break;
                    case Constance.ADAPTER.MSG_COLLECT:
                        CollectEntity collectEntity = (CollectEntity) mData.get(position - 1);
                        TurnHelp.getINSTANCE().turnArt(MsgActivity.this, collectEntity.getId());
                        break;
                }
            }
        });
    }

    @Override
    public void onRightBtClick(View view) {
        super.onRightBtClick(view);
        if (mData.size() > 0) {
            mAdapter.showCb(mType, true);
            mDel.setVisibility(View.VISIBLE);
            mDel.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            mDel.setVisibility(View.GONE);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_msg;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPage = 1;
        getModule(MsgModule.class).getData(mType, mPage);
        mList.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        mPage++;
        getModule(MsgModule.class).getData(mType, mPage);
        mList.loadMoreComplete();
    }

    private void setUpList(List<AbsDEntity> list) {
        if (list != null && list.size() > 0) {
            if (isRefresh) {
                mData.clear();
            }
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
        mBar.getRightBt().setVisibility(mData.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCheck(int position, boolean isChecked) {
        int count = mAdapter.getCheckedNum(mType);
        mDel.setText("删除(" + count + ")");
        mDel.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
        mDel.setBackgroundColor(getResources().getColor(count == 0 ? R.color.transparent : R.color.white));
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (isRefresh) {
            if (obj == null || (obj instanceof List && ((List) obj).size() == 0)) {
                mHint.setVisibility(View.VISIBLE);
                mList.setVisibility(View.GONE);
            }
        }
        hintTempView();
        if (result == ResultCode.SERVER.GET_MSG_DATA) {
            setUpList((List<AbsDEntity>) obj);
        }
    }
}
