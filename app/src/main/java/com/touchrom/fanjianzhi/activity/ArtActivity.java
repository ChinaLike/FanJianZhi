package com.touchrom.fanjianzhi.activity;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.arialyy.frame.util.show.L;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.SimpleViewPagerAdapter;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.databinding.ActivityArtDetailBinding;
import com.touchrom.fanjianzhi.dialog.RewardDialog;
import com.touchrom.fanjianzhi.entity.d_entity.art.ArtContentEntity;
import com.touchrom.fanjianzhi.fragment.ArtCommentFragment;
import com.touchrom.fanjianzhi.fragment.ArtContentFragment;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/5.
 */
public class ArtActivity extends BaseActivity<ActivityArtDetailBinding> {
    @InjectView(R.id.vp)
    ViewPager mVp;
    private int mArtId = -1;
    ArtContentFragment mContentF;
    ArtCommentFragment mCommentF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUseTempView(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mArtId = getIntent().getIntExtra(Constance.KEY.ID, -1);
        if (mArtId == -1) {
            L.e(TAG, "文章ID错误");
            finish();
            return;
        }
        showLoadingDialog();
        setTitle("正文");
        setupContentViewPager();
        mBar.getRightBt().setVisibility(View.VISIBLE);
        mBar.getRightBt().setImageResource(R.mipmap.icon_pay);
        dismissLoadingDialog();
    }

    public void updateComment(int id) {
        mCommentF.update(id);
    }

    /**
     * 初始化内容Viewpager
     */
    private void setupContentViewPager() {
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getSupportFragmentManager());
        mContentF = ArtContentFragment.newInstance(mArtId);
        mCommentF = ArtCommentFragment.newInstance(mArtId);
        adapter.addFrag(mContentF, "正文");
        adapter.addFrag(mCommentF, "评论");
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(2);
    }

    @Override
    public void onBackPressed() {
        if (mVp.getCurrentItem() == 1) {
            mVp.setCurrentItem(0);
            return;
        }
        if (mContentF.getArtIds().size() > 1) {
            mContentF.update();
            mCommentF.update(mContentF.getArtId());
        } else {
            super.onBackPressed();
        }
//        super.onBackPressed();
    }

    @Override
    public void onBackClick(View view) {
        if (mVp.getCurrentItem() == 1) {
            mVp.setCurrentItem(0);
        } else {
            super.onBackClick(view);
        }
    }

    @Override
    public void onRightBtClick(View view) {
        super.onRightBtClick(view);
        if (AppManager.getInstance().isLogin()) {
            ArtContentEntity entity = mContentF.getArtEntity();
            RewardDialog dialog = new RewardDialog(entity.getAuthorId(), entity.getId(), entity.getAuthorName());
            dialog.show(getSupportFragmentManager(), "rewardDialog");
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Glide.get(this).trimMemory(level);
        super.onTrimMemory(level);
    }

    public void setCurrentItem(int position) {
        mVp.setCurrentItem(position);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_art_detail;
    }
}
