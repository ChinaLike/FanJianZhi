package com.touchrom.fanjianzhi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arialyy.absadapter.help.RvItemClickSupport;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.temp.ITempView;
import com.lyy.ui.help.DividerItemDecoration;
import com.lyy.ui.pulltorefresh.xrecyclerview.XRecyclerView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.delegate.MainArtListAdapter;
import com.touchrom.fanjianzhi.base.BaseFragment;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.FragmentTabContentBinding;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.TabEntity;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.module.ChannelModule;
import com.touchrom.fanjianzhi.module.MainModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 */
public class ChannelArtListFragment extends BaseFragment<FragmentTabContentBinding> implements XRecyclerView.LoadingListener {
    @InjectView(R.id.list)
    XRecyclerView mList;
    //    RecyclerView mList;
    private TabEntity mTab;
    private int mPage = 1;
    private boolean mFirst;
    private List<MainContentListEntity> mData = new ArrayList<>();
    private MainArtListAdapter mAdapter;
    private boolean isRefresh = false;
    private int mChannelId;

    public static ChannelArtListFragment newInstance(int channelId, TabEntity tab, boolean showLoadAnim) {
        Bundle args = new Bundle();
        args.putParcelable(Constance.KEY.PARCELABLE_ENTITY, tab);
        args.putBoolean(Constance.KEY.BOOLEAN, showLoadAnim);
        args.putInt(Constance.KEY.ID, channelId);
        ChannelArtListFragment fragment = new ChannelArtListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTab = getArguments().getParcelable(Constance.KEY.PARCELABLE_ENTITY);
        mFirst = getArguments().getBoolean(Constance.KEY.BOOLEAN, true);
        mChannelId = getArguments().getInt(Constance.KEY.ID);
        if (mFirst) {
            intContent();
        }
    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        if (!mFirst) {
            intContent();
        }
    }

    private void intContent() {
        mAdapter = new MainArtListAdapter(getContext(), getChildFragmentManager(), mData);
        LinearLayoutManager lm = new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mList.setLayoutManager(lm);
        mList.setAdapter(mAdapter);
//        DividerItemDecoration divider = new DividerItemDecoration(getContext());
//        divider.setDividerColor(getResources().getColor(R.color.line_color));
//        mList.addItemDecoration(divider);
        if (!mFirst) {
            showTempView(ITempView.LOADING);
        }
        getModule(ChannelModule.class).getChannelArtList(mChannelId, mTab.getId(), mPage);
        mList.setLoadingListener(this);
        mAdapter.setOnItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                TurnHelp.getINSTANCE().artListTurn(getContext(), mData.get(position - 1));
            }
        });
//        RvItemClickSupport.addTo(mList).setOnItemClickListener(new RvItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerView parent, View view, int position, long id) {
//                TurnHelp.getINSTANCE().artListTurn(getContext(), mData.get(position - 1));
//            }
//        });
        hintTempView(2000);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_tab_content;
    }

    private void setUpList(List<MainContentListEntity> list) {
        if (list != null && list.size() > 0) {
            if (isRefresh) {
                mData.clear();
            }
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        isRefresh = true;
        getModule(ChannelModule.class).getChannelArtList(mChannelId, mTab.getId(), mPage);
        mList.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        isRefresh = false;
        getModule(ChannelModule.class).getChannelArtList(mChannelId, mTab.getId(), mPage);
        mList.loadMoreComplete();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        onRefresh();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        onRefresh();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (isRefresh) {
            super.dataCallback(result, obj);
        }
        hintTempView();
        if (result == ResultCode.SERVER.GET_CHANNEL_ART_LIST) {
            setUpList((List<MainContentListEntity>) obj);
        }
    }
}
