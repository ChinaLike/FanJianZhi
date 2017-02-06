package com.touchrom.fanjianzhi.adapter.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.touchrom.fanjianzhi.adapter.delegate.msg_center.CallMeMsgDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.msg_center.CollectDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.msg_center.BaseMsgDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.msg_center.ReplayMsgDelegate;
import com.touchrom.fanjianzhi.adapter.delegate.msg_center.SysMsgDelegate;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.entity.d_entity.msg.MsgDelegateEntity;
import com.touchrom.fanjianzhi.module.MsgModule;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lyy on 2016/6/15.
 */
public class MsgAdapter extends AbsRvDAdapter<AbsDEntity> {

    public MsgAdapter(Context context, List<AbsDEntity> data) {
        super(context, data);
        mManager.addDelegate(new ReplayMsgDelegate(context, this, Constance.ADAPTER.MSG_REPLAY));
        mManager.addDelegate(new SysMsgDelegate(context, this, Constance.ADAPTER.MSG_SYS));
        mManager.addDelegate(new CallMeMsgDelegate(context, this, Constance.ADAPTER.MSG_CALL));
        mManager.addDelegate(new CollectDelegate(context, this, Constance.ADAPTER.MSG_COLLECT));
    }

    /**
     * 获取选择的消息数量
     */
    public int getCheckedNum(int type) {
        BaseMsgDelegate delegation = (BaseMsgDelegate) mManager.getDelegate(type);
        SparseBooleanArray checks = delegation.getCheck();
        Set<Integer> temp = new HashSet<>();
        if (checks != null && checks.size() > 0) {
            try {
                Field f = SparseBooleanArray.class.getDeclaredField("mKeys");
                f.setAccessible(true);
                int[] keys = (int[]) f.get(checks);
                for (int key : keys) {
                    if (checks.get(key)) {
                        temp.add(key);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return temp.size();
    }

    /**
     * 设置checkBox监听
     *
     * @param type
     * @param listener
     */
    public void setOnCheckListener(int type, BaseMsgDelegate.OnCheckListener listener) {
        BaseMsgDelegate delegation = (BaseMsgDelegate) mManager.getDelegate(type);
        delegation.setOnCheckListener(listener);
    }

    /**
     * 选择按钮
     *
     * @param type
     * @param show
     */
    public void showCb(int type, boolean show) {
        BaseMsgDelegate delegation = (BaseMsgDelegate) mManager.getDelegate(type);
        delegation.showDelCb(show);
        notifyDataSetChanged();
    }

    /**
     * 删除消息
     *
     * @param type Constance.ADAPTER.MSG_REPLAY, Constance.ADAPTER.MSG_SYS, Constance.ADAPTER.MSG_CALL
     */
    public void del(int type) {
        BaseMsgDelegate delegation = (BaseMsgDelegate) mManager.getDelegate(type);
        SparseBooleanArray checks = delegation.getCheck();
        Set<Integer> temp = new HashSet<>();
        String ids = "";
        if (checks != null && checks.size() > 0) {
            try {
                Field f = SparseBooleanArray.class.getDeclaredField("mKeys");
                f.setAccessible(true);
                int[] keys = (int[]) f.get(checks);
                for (int key : keys) {
                    if (checks.get(key)) {
                        temp.add(key);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            List<MsgDelegateEntity> entityTemp = new ArrayList<>();
            for (int position : temp) {
                MsgDelegateEntity entity = (MsgDelegateEntity) mData.get(position);
                ids += entity.getMsgId() + ",";
                entityTemp.add(entity);
            }
            //这个需要获取完id后删除
            for (MsgDelegateEntity entity : entityTemp) {
                mData.remove(entity);
            }
            if (!TextUtils.isEmpty(ids)) {
                checks.clear();
                notifyDataSetChanged();
                del(type, ids.substring(0, ids.length() - 1));
            }
        }
    }

    private void del(int type, String ids) {
        MsgModule module = new MsgModule(getContext());
        if (type != Constance.ADAPTER.MSG_COLLECT) {
            delMsg(type, ids, module);
        } else {
            delCollect(ids, module);
        }
    }

    private void delCollect(String ids, MsgModule module) {
        module.delCollect(ids);
    }

    private void delMsg(int type, String ids, MsgModule module) {
        int temp = 1;
        switch (type) {
            case Constance.ADAPTER.MSG_CALL:
                temp = 2;
                break;
            case Constance.ADAPTER.MSG_REPLAY:
                temp = 1;
                break;
            case Constance.ADAPTER.MSG_SYS:
                temp = 3;
                break;
        }
        module.delMsg(temp, ids);
    }
}
