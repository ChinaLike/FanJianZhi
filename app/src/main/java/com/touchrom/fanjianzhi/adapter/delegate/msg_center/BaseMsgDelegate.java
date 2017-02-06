package com.touchrom.fanjianzhi.adapter.delegate.msg_center;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.arialyy.absadapter.delegate.AbsDEntity;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDAdapter;
import com.arialyy.absadapter.delegate.recycler_view.AbsRvDelegation;
import com.arialyy.absadapter.recycler_view.AbsRVHolder;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseRvDelegate;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.dialog.ReplayDialog;
import com.touchrom.fanjianzhi.entity.d_entity.msg.CallMeEntity;
import com.touchrom.fanjianzhi.entity.d_entity.msg.ReplayEntity;

/**
 * Created by lyy on 2016/6/15.
 */
public abstract class BaseMsgDelegate<T extends AbsDEntity, H extends AbsRVHolder> extends BaseRvDelegate<T, H> {
    protected boolean showDelCb = false;
    protected SparseBooleanArray mCheck = new SparseBooleanArray();
    OnCheckListener mListener;

    public void setOnCheckListener(OnCheckListener listener) {
        mListener = listener;
    }

    public interface OnCheckListener {
        public void onCheck(int position, boolean isChecked);
    }

    public BaseMsgDelegate(Context context, AbsRvDAdapter adapter, int itemType) {
        super(context, adapter, itemType);
    }

    public SparseBooleanArray getCheck() {
        return mCheck;
    }

    public void showDelCb(boolean show) {
        showDelCb = show;
    }

    protected class ReplayBtListener implements View.OnClickListener {
        CallMeEntity callMeEntity;
        ReplayEntity replayEntity;

        public void setCallMeEntity(CallMeEntity entity) {
            this.callMeEntity = entity;
        }

        public void setReplayEntity(ReplayEntity entity) {
            this.replayEntity = entity;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt) {
                int id = 0;
                String nikeName = "";
                if (getItemViewType() == Constance.ADAPTER.MSG_CALL) {
                    id = callMeEntity.getCommentId();
                    nikeName = callMeEntity.getNikeName();
                } else if (getItemViewType() == Constance.ADAPTER.MSG_REPLAY) {
                    id = replayEntity.getCommentId();
                    nikeName = replayEntity.getCmNikeName();
                }
                ReplayDialog dialog = new ReplayDialog(id, nikeName);
                dialog.show(getFm(), "replay_dialog");
            }
        }
    }

    protected class CbListener implements CheckBox.OnCheckedChangeListener {
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mCheck.put(position, isChecked);
            if (mListener != null) {
                mListener.onCheck(position, isChecked);
            }
        }
    }

}
