package com.touchrom.fanjianzhi.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;

import com.arialyy.absadapter.listview.AbsSimpleAdapter;
import com.arialyy.absadapter.listview.AbsSimpleViewHolder;
import com.arialyy.frame.util.show.L;
import com.lyy.ui.widget.NoScrollGridView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.LoginActivity;
import com.touchrom.fanjianzhi.base.AppManager;
import com.touchrom.fanjianzhi.base.BaseDialog;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.databinding.DialogShareBinding;
import com.touchrom.fanjianzhi.entity.ShareEntity;
import com.touchrom.fanjianzhi.entity.SimpleAdapterEntity;
import com.touchrom.fanjianzhi.help.RippleHelp;
import com.touchrom.fanjianzhi.module.ArtModule;
import com.touchrom.fanjianzhi.util.S;
import com.touchrom.fanjianzhi.util.ShareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by lyy on 2015/12/3.
 * 分享对话框
 */
@SuppressLint("ValidFragment")
public class ShareDialog extends BaseDialog<DialogShareBinding> implements PlatformActionListener, View.OnClickListener {
    private static final int WEIXIN = 0x01;
    private static final int WEIXIN_MOMENT = 0x02;
    private static final int SINA = 0x03;
    private static final int QQ = 0x04;
    private static final int QZONE = 0x05;
    private static final int SYSTEM = 0x06;
    @InjectView(R.id.grid)
    NoScrollGridView mGrid;
    @InjectView(R.id.cancel)
    Button mCancel;
    @InjectView(R.id.collect)
    Button mCollect;
    private int mCurrentType = 0;
    private ShareEntity mShareEntity;
    private int mType = -1;
    private int mId = 0;

    private ShareDialog() {

    }

    /**
     * @param type 1：文章分享，2：app分享
     * @param id
     */
    public ShareDialog(int type, int id) {
        mType = type;
        mId = id;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(p);
        getDialog().getWindow().setWindowAnimations(R.style.dialogAnim);

        if (mType == 1) {
            mCollect.setVisibility(View.VISIBLE);
            getModule(ArtModule.class).getCollectState(mId);
        } else {
            mCollect.setVisibility(View.GONE);
        }
        getModule(ArtModule.class).getShareData(mId, mType);
        initWidget();
    }

    private void initWidget() {
        mGrid.setAdapter(new AbsSimpleAdapter<SimpleAdapterEntity>(getContext(), setData(), R.layout.item_share) {
            @Override
            public void convert(AbsSimpleViewHolder helper, SimpleAdapterEntity item) {
                helper.setImageResource(R.id.img, item.getArg());
                helper.setText(R.id.text, item.getMsg());
            }
        });
        RippleHelp.createRipple(getContext(), mCancel);
        mCollect.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentType = position;
//                T.showShort(mActivity, "正在后台执行分享...");
                share(position);
                dismiss();
            }
        });
    }

    /**
     * 执行分享
     *
     * @param type
     */
    private void share(int type) {
        if (mShareEntity == null) {
            S.i(mRootView, "正在获取分享数据，请重试");
            return;
        }
        switch (type) {
            case 0:
                ShareUtil.getInstance().shareToWeixin(getShareEntity(WEIXIN), this);
                break;
            case 1:
                ShareUtil.getInstance().shareToWeixinMoment(getShareEntity(WEIXIN_MOMENT), this);
                break;
            case 2:
                ShareUtil.getInstance().shareToWeibo(getShareEntity(SINA), this);
                break;
            case 3:
                ShareUtil.getInstance().shareToQQ(getShareEntity(QQ), this);
                break;
            case 4:
                ShareUtil.getInstance().shareToQzone(getShareEntity(QZONE), this);
                break;
            case 5:
                ShareEntity.ParamsEntity entity = getShareEntity(SYSTEM);
                ShareUtil.getInstance().shareToSystem(mActivity, "我刚刚下载了【" + entity.getTitle() + "】，快来一起玩吧！" + entity.getUrl());
                break;
        }
    }

    /**
     * 设置分享实体
     *
     * @param type {@link #WEIXIN_MOMENT}
     * @return
     */
    private ShareEntity.ParamsEntity getShareEntity(int type) {
        ShareEntity.ParamsEntity entity = new ShareEntity.ParamsEntity();
        switch (type) {
            case WEIXIN:
                entity = mShareEntity.getWeixin();
                break;
            case WEIXIN_MOMENT:
                entity = mShareEntity.getWeixinMoment();
                break;
            case SINA:
                entity = mShareEntity.getWeibo();
                break;
            case QQ:
                entity = mShareEntity.getQq();
                break;
            case QZONE:
                entity = mShareEntity.getqZone();
                break;
            case SYSTEM:
                entity = mShareEntity.getSystem();
                break;
        }
        return entity;
    }

    private List<SimpleAdapterEntity> setData() {
        List<SimpleAdapterEntity> list = new ArrayList<>();
        int[] imgs = new int[]{
                R.mipmap.icon_share_wx,
                R.mipmap.icon_share_wx_moment,
                R.mipmap.icon_share_weibo,
                R.mipmap.icon_share_qq,
                R.mipmap.icon_share_qzone,
                R.mipmap.icon_share_more
        };
        String[] texts = new String[]{
                "微信好友", "微信朋友圈", "新浪微博", "QQ好友", "QQ空间", "更多"
        };

        int i = 0;
        for (int img : imgs) {
            SimpleAdapterEntity entity = new SimpleAdapterEntity();
            entity.setMsg(texts[i]);
            entity.setArg(img);
            list.add(entity);
            i++;
        }

        return list;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        L.m(hashMap);
        S.i(mActivity.getRootView(), "分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        S.i(mActivity.getRootView(), "分享失败，请检查网络");
        Snackbar.make(mRootView, "分享失败", Snackbar.LENGTH_LONG)
                .setAction("点击重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share(mCurrentType);
                    }
                });
    }

    @Override
    public void onCancel(Platform platform, int i) {
        L.e(TAG, "cancel");
        L.d(TAG, "i = " + i);
        S.i(mActivity.getRootView(), "分享取消");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                break;
            case R.id.collect:
                if (!AppManager.getInstance().isLogin()) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                getModule(ArtModule.class).artCollect(mId);
                break;
        }
        dismiss();
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        if (result == ResultCode.SERVER.GET_SHARE_DATA) {
            mShareEntity = (ShareEntity) obj;
            ShareEntity.ParamsEntity systemEntity = new ShareEntity.ParamsEntity();
            systemEntity.setTitle(mShareEntity.getQq().getTitle());
            systemEntity.setUrl(mShareEntity.getQq().getUrl());
            mShareEntity.setSystem(systemEntity);
        } else if (result == ResultCode.SERVER.GET_COLLECT_STATE) {
            boolean isCollect = (boolean) obj;
            mCollect.setEnabled(!isCollect);
            mCollect.setText(isCollect ? "已收藏" : "加入收藏");
        }
    }

}
