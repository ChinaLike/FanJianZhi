package com.touchrom.fanjianzhi.base;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDelegation;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.entity.SettingEntity;

/**
 * Created by lyy on 2016/6/15.
 */
public abstract class BaseRvDelegate<T extends AbsDEntity, H extends AbsRVHolder> extends AbsRvDelegation<T, H> {

    protected SettingEntity mSettingEntity;

    public BaseRvDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    public void updateSetting(){
        mSettingEntity = AppManager.getInstance().getSetting();
    }

    protected FragmentManager getFm() {
        if (getContext() instanceof BaseActivity) {
            return ((BaseActivity) getContext()).getSupportFragmentManager();
        }
        return null;
    }
}
