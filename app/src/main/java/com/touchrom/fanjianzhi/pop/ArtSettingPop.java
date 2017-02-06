package com.touchrom.fanjianzhi.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.ScreenUtil;
import com.arialyy.frame.util.SharePreUtil;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.base.BasePopupWindow;
import com.touchrom.fanjianzhi.config.Constance;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/13.
 * 文章设置悬浮框
 */
public class ArtSettingPop extends BasePopupWindow implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @InjectView(R.id.seek)
    SeekBar mSeek;
    @InjectView(R.id.ll)
    LinearLayout mLl;
    @InjectView(R.id.mini)
    TextView mMini;
    @InjectView(R.id.medium)
    TextView mMedium;
    @InjectView(R.id.larger)
    TextView mLarger;
    @InjectView(R.id.temp)
    View mTemp;

    private OnFontSizeChangeListener mListener;

    public interface OnFontSizeChangeListener {
        public void onChange(View view, float fontSize);
    }

    public ArtSettingPop(Context context) {
        super(context, new ColorDrawable(context.getResources().getColor(R.color.rippleDefaultColor)));
//        setHeight((int) getContext().getResources().getDimension(R.dimen.bt_h_48) * 2);
        int h = AndroidUtils.getScreenParams(getContext())[1] - AndroidUtils.getNavigationBarHeight(getContext());
        setHeight(h);
        init();
    }

    public void setOnFontSizeChangeListener(@NonNull OnFontSizeChangeListener listener) {
        mListener = listener;
    }

    protected void init() {
        super.init();
        int size = SharePreUtil.getInt(Constance.APP.NAME, getContext(), "fontSize");
        if (size == 14) {
            selectedView(mMini);
        } else if (size == 16) {
            selectedView(mMedium);
        } else {
            selectedView(mLarger);
        }
        Context context = getContext();
        if (context instanceof Activity) {
            BaseActivity activity = (BaseActivity) context;
            float h = ScreenUtil.getInstance().getScreenBrightness(activity);
            mSeek.setProgress((int) h);
        }
        mMini.setOnClickListener(this);
        mMedium.setOnClickListener(this);
        mLarger.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        mTemp.setOnClickListener(this);
    }

    private void selectedView(View view) {
        for (int i = 0, count = mLl.getChildCount(); i < count; i++) {
            View view1 = mLl.getChildAt(i);
            view1.setSelected(view.getId() == view1.getId());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Context context = getContext();
        if (context instanceof Activity) {
            ScreenUtil.getInstance().setBrightness((BaseActivity) context, seekBar.getProgress());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        float size = 14;
        switch (v.getId()) {
            case R.id.mini:
                size = 14;
                break;
            case R.id.medium:
                size = 16;
                break;
            case R.id.larger:
                size = 18;
                break;
            case R.id.temp:
                dismiss();
                return;
        }
        selectedView(v);
        if (mListener != null) {
            mListener.onChange(v, size);
        }
        SharePreUtil.putInt(Constance.APP.NAME, getContext(), "fontSize", (int) size);
        dismiss();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.pop_art_setting;
    }
}
