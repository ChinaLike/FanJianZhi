package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDelegation;
import com.arialyy.absadapter.recycler_view.AbsRVAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.lyy.ui.widget.AutoScrollViewPager;
import com.lyy.ui.widget.CircleIndicator;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.SimpleViewPagerAdapter;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.entity.d_entity.BannerEntity;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.fragment.BannerFragment;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/6.
 */
public class BannerDelegate extends BaseArtDelegate<MainContentListEntity, BannerDelegate.BannerHolder> {
    private FragmentManager mFm;

    public BannerDelegate(Context context, AbsRvDAdapter adapter, int itemType, FragmentManager fm) {
        super(context, adapter, itemType);
        mFm = fm;
    }

    @Override
    public BannerHolder createHolder(View convertView) {
        return new BannerHolder(convertView);
    }

    @Override
    public void bindData(int position, BannerHolder holder, MainContentListEntity item) {
        SimpleViewPagerAdapter adapter = (SimpleViewPagerAdapter) holder.vp.getTag(holder.vp.getId());
        if (adapter == null) {
            adapter = new SimpleViewPagerAdapter(mFm);
            holder.vp.setTag(holder.vp.getId(), adapter);
            List<BannerEntity> banners = item.getBanner().getContent();
            int i = 0;
            for (BannerEntity entity : banners) {
                BannerFragment fragment = BannerFragment.newInstance(entity);
                fragment.setCanClick(true);
                adapter.addFrag(fragment, i + "banner");
                i ++;
            }
            holder.vp.setAdapter(adapter);
            holder.vp.startAutoScroll();
            holder.vp.setInterval(4000);
            holder.indicator.setViewPager(holder.vp);
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_banner;
    }

    class BannerHolder extends AbsRVHolder {
        @InjectView(R.id.vp)
        AutoScrollViewPager vp;
        @InjectView(R.id.indicator)
        CircleIndicator indicator;

        public BannerHolder(View itemView) {
            super(itemView);
        }
    }
}
