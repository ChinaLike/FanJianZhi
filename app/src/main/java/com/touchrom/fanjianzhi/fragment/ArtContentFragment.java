package com.touchrom.fanjianzhi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.help.RvItemClickSupport;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.arialyy.frame.temp.ITempView;
import com.arialyy.frame.util.KeyBoardUtils;
import com.arialyy.frame.util.show.T;
import com.bumptech.glide.Glide;
import com.lyy.ui.help.DividerItemDecoration;
import com.lyy.ui.widget.IconEditText;
import com.lyy.ui.widget.IconText;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.ArtActivity;
import com.touchrom.fanjianzhi.activity.LoginActivity;
import com.touchrom.fanjianzhi.adapter.delegate.ArtDetailAdapter;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseFragment;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.FragmentArtContentBinding;
import com.touchrom.fanjianzhi.dialog.ShareDialog;
import com.touchrom.fanjianzhi.entity.d_entity.CommentEntity;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.d_entity.TextEntity;
import com.touchrom.fanjianzhi.entity.d_entity.art.ArtBarEntity;
import com.touchrom.fanjianzhi.entity.d_entity.art.ArtContentEntity;
import com.touchrom.fanjianzhi.module.ArtModule;
import com.touchrom.fanjianzhi.pop.ArtSettingPop;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/13.
 * 文章详情
 */
public class ArtContentFragment extends BaseFragment<FragmentArtContentBinding> implements View.OnClickListener,
        ArtSettingPop.OnFontSizeChangeListener {
    @InjectView(R.id.bottom_bar)
    RelativeLayout mBottomBar;
    @InjectView(R.id.list)
    RecyclerView mList;
    @InjectView(R.id.comment_et)
    IconEditText mCommentEt;
    @InjectView(R.id.comment)
    IconText mCommentBt;
    @InjectView(R.id.share)
    ImageView mShare;
    @InjectView(R.id.more)
    ImageView mMore;

    private int mArtId = -1;
    private List<AbsDEntity> mData = new ArrayList<>();
    private ArtDetailAdapter mAdapter;
    private ArtContentEntity mArtEntity;
    Stack<Integer> mArtIds = new Stack<>();

    public static ArtContentFragment newInstance(int artId) {

        Bundle args = new Bundle();
        args.putInt(Constance.KEY.ID, artId);
        ArtContentFragment fragment = new ArtContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mArtId = getArguments().getInt(Constance.KEY.ID);
        mArtIds.add(mArtId);
//        showTempView(ITempView.LOADING);
        mAdapter = new ArtDetailAdapter(getContext(), mData);
        LinearLayoutManager manager = new LinearLayoutManager(getContext()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        manager.setAutoMeasureEnabled(false);
        mList.setHasFixedSize(false);
        mList.setLayoutManager(manager);
//        DividerItemDecoration divider = new DividerItemDecoration(getContext());
//        divider.setDividerColor(getResources().getColor(R.color.line_color));
//        mList.addItemDecoration(divider);
        mList.setAdapter(mAdapter);
        getModule(ArtModule.class).getArtDetail(mArtId);

//        RvItemClickSupport.addTo(mList).setOnItemClickListener(new RvItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerView parent, View view, int position, long id) {
//                if (position == 0) {
//                    return;
//                }
//                AbsDEntity entity = mData.get(position);
//                if (entity instanceof MainContentListEntity) {
////                    TurnHelp.getINSTANCE().artListTurn(getContext(), (MainContentListEntity) entity);
//                    int artId = getEntityId((MainContentListEntity) entity);
//                    mArtIds.push(artId);
////                    showTempView(ITempView.LOADING);
//                    show(artId);
//                }
//            }
//        });

        mAdapter.setOnItemClickListener(new AbsRVHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                if (position == 0) {
                    return;
                }
                AbsDEntity entity = mData.get(position);
                if (entity instanceof MainContentListEntity) {
//                    TurnHelp.getINSTANCE().artListTurn(getContext(), (MainContentListEntity) entity);
                    int id = getEntityId((MainContentListEntity) entity);
                    mArtIds.push(id);
//                    showTempView(ITempView.LOADING);
                    show(id);
                } else if (entity instanceof CommentEntity || entity instanceof TextEntity) {
                    ((ArtActivity) mActivity).setCurrentItem(1);
                }
            }
        });

        mMore.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mCommentBt.setOnClickListener(this);
        mCommentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (TextUtils.isEmpty(mCommentEt.getText())) {
                        return false;
                    }
                    if (!AppManager.getInstance().isLogin()) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else {
                        getModule(ArtModule.class).comment(1, mArtId, mCommentEt.getText().toString());
                    }

                }
                return false;
            }
        });
    }

    public int getEntityId(MainContentListEntity entity) {
        int id = -1;
        switch (entity.getType()) {
            case Constance.ADAPTER.CHANNEL_CONTENT_ITEM:
                id = entity.getChannelContent().getId();
                break;
            case Constance.ADAPTER.IMG_CONTENT_ITEM:
                id = entity.getImgContent().getId();
                break;
            case Constance.ADAPTER.IMG_TEXT_CONTENT_ITEM:
                id = entity.getImgTextContent().getId();
                break;
            case Constance.ADAPTER.VIDEO_CONTENT_ITEM:
                id = entity.getVideoContent().getId();
                break;
            case Constance.ADAPTER.TEXT_CONTENT_ITEM:
                id = entity.getTextContent().getId();
                break;
        }
        return id;
    }

    public void update() {
        int id = mArtIds.pop();
        if (id == mArtId) {
            update();
            return;
        }
        show(id);
    }

    private void show(int id) {
        Glide.get(getContext()).clearMemory();
        showLoadingDialog();
        mArtId = id;
        mData.clear();
        getModule(ArtModule.class).getArtDetail(id);
        ((ArtActivity) mActivity).updateComment(id);
        hintTempView();
        dismissLoadingDialog();
    }

    public int getArtId() {
        return mArtId;
    }

    public Stack<Integer> getArtIds() {
        return mArtIds;
    }

    public ArtContentEntity getArtEntity() {
        return mArtEntity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                shareSetting();
                break;
            case R.id.reward:
                break;
            case R.id.share:
                ShareDialog shareDialog = new ShareDialog(1, mArtId);
                shareDialog.show(getChildFragmentManager(), "分享对话框");
                break;
            case R.id.comment:
                ((ArtActivity) mActivity).setCurrentItem(1);
                break;
        }
    }

    private void shareSetting() {
        ArtSettingPop pop = new ArtSettingPop(getContext());
        pop.setOnFontSizeChangeListener(this);
//        int y = (int) (AndroidUtils.getScreenParams(getContext())[1] - (getResources().getDimension(R.dimen.bt_h_50) * 2) - AndroidUtils.getNavigationBarHeight(getContext()));
//        pop.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, y);
//        int y = mBottomBar.getHeight() + AndroidUtils.getNavigationBarHeight(getContext());
        pop.showAtLocation(mBottomBar, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * 添加文章内容
     *
     * @param entity
     */
    private void addContent(ArtContentEntity entity) {
        mData.add(0, entity);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加推荐内容
     */
    private void addRecommend(List<MainContentListEntity> entities) {
        mData.addAll(entities);
    }

    private void addBar(String title) {
        ArtBarEntity entity = new ArtBarEntity();
        entity.setTitle(title);
        mData.add(entity);
    }

    private void addHotComment(List<CommentEntity> hotComment) {
        mData.addAll(hotComment);
        mData.add(new TextEntity());
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_art_content;
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        if (result == ResultCode.SERVER.GET_ART_DETAIL) {
            super.dataCallback(result, obj);
            if (obj == null) {
                return;
            }
            mArtEntity = (ArtContentEntity) obj;
            mCommentBt.setText(mArtEntity.getCommentNum() + "");
            addContent(mArtEntity);
            getModule(ArtModule.class).getHotComment(mArtEntity.getId());
        } else if (result == ResultCode.SERVER.GET_ART_RECOMMEND) {
            if (obj != null) {
                addBar("相关推荐");
                addRecommend((List<MainContentListEntity>) obj);
            }
            mAdapter.notifyDataSetChanged();
        } else if (result == ResultCode.SERVER.ART_COMMENT) {
            mCommentEt.setText("");
            KeyBoardUtils.closeKeybord(mCommentEt, getContext());
            T.showShort(getContext(), "评论成功");
        } else if (result == ResultCode.SERVER.GET_HOT_COMMENT) {
            List<CommentEntity> comments = (List<CommentEntity>) obj;
            if (comments != null && comments.size() > 0) {
                addBar("热门评论");
                addHotComment(comments);
            }
            getModule(ArtModule.class).getArtRecommend(mArtEntity.getArtType());
        }
    }

    @Override
    public void onChange(View view, float fontSize) {
        mAdapter.setRichTextSize(fontSize);
    }
}
