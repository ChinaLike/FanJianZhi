package com.touchrom.fanjianzhi.adapter.delegate.art_list_content;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.dialog.ReplayDialog;
import com.touchrom.fanjianzhi.entity.d_entity.msg.CallMeEntity;
import com.touchrom.fanjianzhi.entity.d_entity.msg.ReplayEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lyy on 2016/6/15.
 */
public abstract class BaseArtDelegate<T extends AbsDEntity, H extends AbsRVHolder> extends BaseRvDelegate<T, H> {
    String mKey;

    public BaseArtDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    public void setKey(String key) {
        mKey = key;
    }

    protected synchronized CharSequence tintKey(String str, String key) {
        int color = getContext().getResources().getColor(R.color.highlight);
        return highlightKeyword(str, key, color);
    }



    /**
     * 高亮所有关键字
     *
     * @param str 这个字符串
     * @param key 关键字
     */
    private synchronized SpannableString highlightKeyword(String str, String key, int highlightColor) {
        if (!str.contains(key)) {
            return null;
        }
        SpannableString sp = new SpannableString(str);
        key = Pattern.quote(key);
        Pattern p = Pattern.compile(key);
        Matcher m = p.matcher(str);

        while (m.find()) {  //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            sp.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }
}
