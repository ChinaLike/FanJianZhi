package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;
import com.arialyy.frame.util.show.T;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.adapter.SimpleViewPagerAdapter;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.databinding.DialogImgBrowseBinding;
import com.touchrom.fanjianzhi.fragment.ImgBrowseFragment;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/2/2.
 * 图片浏览对话框
 */
@TargetApi(11)
@SuppressLint("ValidFragment")
public class ImgBrowseDialog extends BaseDialog<DialogImgBrowseBinding> implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;
    @InjectView(R.id.view_pager)
    ViewPager mVp;
    @InjectView(R.id.download)
    ImageView mDownload;
    private List<String> mImgs;
    private int mPosition = 0;
    private int mCurrentP = 0;

    public ImgBrowseDialog(List<String> imgs) {
        mImgs = imgs;
    }

    public ImgBrowseDialog(List<String> imgs, int position) {
        mImgs = imgs;
        mPosition = position;
        mCurrentP = position;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_img_browse;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mVp.setPageTransformer(true, this);
        SimpleViewPagerAdapter adapter = new SimpleViewPagerAdapter(getChildFragmentManager());
        int i = 0;
        for (String img : mImgs) {
            adapter.addFrag(ImgBrowseFragment.newInstance(img), "img" + i);
            i++;
        }
        mVp.setAdapter(adapter);
        mVp.setCurrentItem(mPosition);
        mVp.setOffscreenPageLimit(4);
        getBinding().setCount((mPosition + 1) + "/" + mImgs.size());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getBinding().setCount((position + 1) + "/" + mImgs.size());
                mCurrentP = position;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mImgs.get(mCurrentP);
                int end = url.lastIndexOf(".");
                int start = end - 32;
                String name = url.substring(start, end);
                DownLoadUtil util = new DownLoadUtil();
                util.download(getContext(), url, Constance.PATH.IMG_DIR + name + ".png", new DownloadListener() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Looper.prepare();
                        Looper.myLooper();
                        T.showLong(getContext(), "图片已下载到: " + Constance.PATH.IMG_DIR + "，文件夹");
                        Looper.loop();
                    }
                });
            }
        });
    }

    @Override
    public void dismiss() {
        Glide.get(getContext()).clearMemory();
        Glide.get(getContext()).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
        super.dismiss();
    }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position < -1) {
            view.setAlpha(0);

        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {
            view.setAlpha(0);
        }
    }
}
