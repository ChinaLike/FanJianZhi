package com.touchrom.fanjianzhi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.frame.util.KeyBoardUtils;
import com.arialyy.frame.util.show.T;
import com.lyy.ui.pulltorefresh.xrecyclerview.XRecyclerView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.LoginActivity;
import com.touchrom.fanjianzhi.adapter.ArtCommentAdapter;
import com.touchrom.fanjianzhi.adapter.delegate.common.CommentDelegate;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseFragment;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.FragmentArtCommentBinding;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;
import com.touchrom.fanjianzhi.module.ArtModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/14.
 * 评论列表
 */
public class ArtCommentFragment extends BaseFragment<FragmentArtCommentBinding> implements XRecyclerView.LoadingListener,
        CommentDelegate.CommentClickListener, View.OnClickListener {
    @InjectView(R.id.list)
    XRecyclerView mList;
    @InjectView(R.id.replay_et)
    EditText mReplayEt;
    @InjectView(R.id.commit)
    ImageView mCommit;
    @InjectView(R.id.hint)
    TextView mHint;
    private boolean iSRefresh = true;
    private List<AbsDEntity> mData = new ArrayList<>();
    private int mArtId;
    private int mPage = 1;
    private ArtCommentAdapter mAdapter;
    private int mPosition;
    private int mCmtId;
    private boolean isReplay = false;

    public static ArtCommentFragment newInstance(int artId) {

        Bundle args = new Bundle();
        args.putInt(Constance.KEY.ID, artId);
        ArtCommentFragment fragment = new ArtCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ArtCommentFragment() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mArtId = getArguments().getInt(Constance.KEY.ID);
        mAdapter = new ArtCommentAdapter(getContext(), mData);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setAutoMeasureEnabled(false);
        mList.setHasFixedSize(false);
        mList.setLayoutManager(manager);
//        DividerItemDecoration divider = new DividerItemDecoration(getContext());
//        divider.setDividerColor(getResources().getColor(R.color.line_color));
//        mList.addItemDecoration(divider);
        mList.setAdapter(mAdapter);
        mAdapter.setCommentClickListener(this);
        mCommit.setOnClickListener(this);
        mList.setLoadingListener(this);
        getModule(ArtModule.class).getCommentList(mArtId, mPage);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_art_comment;
    }

    @Override
    public void onRefresh() {
        iSRefresh = true;
        mPage = 1;
        getModule(ArtModule.class).getCommentList(mArtId, mPage);
        mList.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        iSRefresh = false;
        mPage++;
        getModule(ArtModule.class).getCommentList(mArtId, mPage);
        mList.loadMoreComplete();
    }

    private void setUpList(List<CommentEntity> lists) {
        if (lists != null && lists.size() > 0) {
            if (iSRefresh) {
                mData.clear();
            }
            mData.addAll(lists);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCommentClick(int position, int cmtId, String nikeName) {
        KeyBoardUtils.openKeybord(mReplayEt, getContext());
        mReplayEt.setHint("@" + nikeName);
        mPosition = position;
        mCmtId = cmtId;
        isReplay = true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.commit) {
            if (!AppManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            } else if (!TextUtils.isEmpty(mReplayEt.getText())) {
                getModule(ArtModule.class).comment(isReplay ? 2 : 1, isReplay ? mCmtId : mArtId, mReplayEt.getText().toString());
            }
        }
    }

    public void update(int artId) {
        mArtId = artId;
        onRefresh();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (iSRefresh) {
            if (obj != null && obj instanceof List) {
                if (((List) obj).size() == 0) {
                    mList.setVisibility(View.GONE);
                    mHint.setVisibility(View.VISIBLE);
                } else {
                    mHint.setVisibility(View.GONE);
                    mList.setVisibility(View.VISIBLE);
                }
            }
        }
        if (result == ResultCode.SERVER.GET_COMMENT_LIST) {
            setUpList((List<CommentEntity>) obj);
        } else if (result == ResultCode.SERVER.ART_COMMENT) {
            mReplayEt.setText("");
            KeyBoardUtils.closeKeybord(mReplayEt, getContext());
            T.showShort(getContext(), "评论成功");
            mHint.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
            if (isReplay) {
                mAdapter.updateReplay(mPosition, (CommentEntity) obj);
            } else {
                onRefresh();
            }
        }
    }
}
