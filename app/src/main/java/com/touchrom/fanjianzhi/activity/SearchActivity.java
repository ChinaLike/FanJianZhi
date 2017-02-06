package com.touchrom.fanjianzhi.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arialyy.absadapter.help.RvItemClickSupport;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.temp.ITempView;
import com.lyy.ui.help.DividerItemDecoration;
import com.lyy.ui.pulltorefresh.xrecyclerview.XRecyclerView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.delegate.MainArtListAdapter;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.ActivitySearchBinding;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.module.SearchModule;
import com.touchrom.fanjianzhi.pop.SearchPop;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/12.
 * 搜索列表
 */
public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements XRecyclerView.LoadingListener, SearchPop.OnSearchKeyListener {
    @InjectView(R.id.list)
    XRecyclerView mList;
    private String mKey;
    private int mPage = 1;
    private List<MainContentListEntity> mData = new ArrayList<>();
    private MainArtListAdapter mAdapter;
    private boolean isRefresh = true;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mKey = getIntent().getStringExtra(Constance.KEY.STRING);
        mList.setPullRefreshEnabled(false);
        if (mKey != null) {
            showTempView(ITempView.LOADING);
            getModule(SearchModule.class).searchContent(mKey, mPage);
        }
        mList.setLoadingListener(this);
        mAdapter = new MainArtListAdapter(this, getSupportFragmentManager(), mData);
        mAdapter.setOnItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                TurnHelp.getINSTANCE().artListTurn(SearchActivity.this, mData.get(position - 1));
            }
        });
//        RvItemClickSupport.addTo(mList).setOnItemClickListener(new RvItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerView parent, View view, int position, long id) {
//                TurnHelp.getINSTANCE().artListTurn(SearchActivity.this, mData.get(position - 1));
//            }
//        });
        DividerItemDecoration divider = new DividerItemDecoration(this);
        divider.setDividerColor(getResources().getColor(R.color.line_color));
//        divider.setDividerSpace(getResources().getDimension(R.dimen.item_divider));
        mList.addItemDecoration(divider);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);
        setTitle("搜索");
        mBar.getRightBt().setImageResource(R.mipmap.icon_search);
        mBar.getRightBt().setVisibility(View.VISIBLE);
    }

    @Override
    public void onRightBtClick(View view) {
        super.onRightBtClick(view);
        SearchPop pop = new SearchPop(this, false);
        pop.setOnSearchKeyListener(this);
        pop.showAtLocation(getRootView(), Gravity.NO_GRAVITY, 0, 0);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPage = 1;
        showTempView(ITempView.LOADING);
        getModule(SearchModule.class).searchContent(mKey, mPage);
        mList.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        mPage++;
        getModule(SearchModule.class).searchContent(mKey, mPage);
        mList.loadMoreComplete();
    }

    @Override
    protected void onNetDataNull() {
        super.onNetDataNull();
        onRefresh();
    }

    @Override
    protected void onNetError() {
        super.onNetError();
        finish();
    }

    private void setUpList(List<MainContentListEntity> list) {
        if (list != null && list.size() > 0) {
            if (isRefresh) {
                mData.clear();
            }
            mAdapter.setKey(mKey);
            mData.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchKey(String key) {
        showLoadingDialog();
        isRefresh = true;
        mPage = 1;
        mKey = key;
        getModule(SearchModule.class).searchContent(mKey, mPage);
        dismissLoadingDialog();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (isRefresh) {
            super.dataCallback(result, obj);
        } else {
            hintTempView();
        }
        if (result == ResultCode.SERVER.SEARCH_CONTENT) {
            setUpList((List<MainContentListEntity>) obj);
        }
    }

}
