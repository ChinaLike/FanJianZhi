package com.touchrom.fanjianzhi.pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.arialyy.absadapter.listview.AbsSimpleAdapter;
import com.arialyy.absadapter.listview.AbsSimpleViewHolder;
import com.arialyy.frame.util.show.T;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.activity.SearchActivity;
import com.touchrom.fanjianzhi.adapter.SearchHistoryAdapter;
import com.touchrom.fanjianzhi.base.BasePopupWindow;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.entity.SearchAdapterEntity;
import com.touchrom.fanjianzhi.entity.sql.SearchHistoryEntity;
import com.touchrom.fanjianzhi.module.SearchModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/4.
 * 搜索悬浮框
 */
public class SearchPop extends BasePopupWindow implements View.OnClickListener {
    @InjectView(R.id.back)
    ImageView mBack;
    @InjectView(R.id.clean)
    ImageView mClean;
    @InjectView(R.id.search)
    ImageView mSearch;
    @InjectView(R.id.search_et)
    EditText mEt;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.temp)
    View mTemp;
    List<SearchAdapterEntity> mData;
    SearchHistoryAdapter mAdapter;
    private boolean isSearchKey = true;
    private OnSearchKeyListener mListener;

    public interface OnSearchKeyListener {
        public void onSearchKey(String key);
    }

    public SearchPop(Context context, boolean isSearchKey) {
        super(context, new ColorDrawable(context.getResources().getColor(R.color.rippleDefaultColor)));
        this.isSearchKey = isSearchKey;
        initWidget();
    }

    private void initWidget() {
        mData = new ArrayList<>();
        mBack.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        mTemp.setOnClickListener(this);
        mAdapter = new SearchHistoryAdapter(getContext(), mData);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = mData.get(position).getText().toString();
                handleClick(key);
            }
        });

        mEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    getModule(SearchModule.class).getNetSearchData(s.toString());
                } else {
                    getModule(SearchModule.class).getSearchHistory();
                }
            }
        });

        getModule(SearchModule.class).getSearchHistory();
    }

    public void setOnSearchKeyListener(OnSearchKeyListener listener) {
        mListener = listener;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.pop_search;
    }

    private void handleClick(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SearchHistoryEntity entity = new SearchHistoryEntity();
        entity.setTitle(key);
        entity.setTime(System.currentTimeMillis());
        getModule(SearchModule.class).saveSearchData(entity);
        if (!isSearchKey) {
            if (mListener != null) {
                mListener.onSearchKey(key);
            }
        } else {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra(Constance.KEY.STRING, key);
            getContext().startActivity(intent);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.clean:
                mEt.setText("");
                break;
            case R.id.search:
                String key = mEt.getText().toString();
                handleClick(key);
                break;
            case R.id.temp:
                dismiss();
                break;
        }
    }

    @Override
    protected void dataCallback(int result, Object obj) {
        super.dataCallback(result, obj);
        List<CharSequence> list = (List<CharSequence>) obj;
        if (result == ResultCode.SERVER.SEARCH_HISTORY) {
            mData.clear();
            for (CharSequence str : list) {
                SearchAdapterEntity entity = new SearchAdapterEntity();
                entity.setText(str);
                entity.setType(1);
                entity.setImg(R.mipmap.icon_search_history);
                mData.add(entity);
            }
            if (list.size() > 0) {
                SearchAdapterEntity entity = new SearchAdapterEntity();
                entity.setText("清除历史记录");
                entity.setType(2);
                mData.add(entity);
            }
            mAdapter.notifyDataSetChanged();
        } else if (result == ResultCode.SERVER.GET_SEARCH_KEY_DATA) {
            mData.clear();
            for (CharSequence str : list) {
                SearchAdapterEntity entity = new SearchAdapterEntity();
                entity.setText(str);
                entity.setType(1);
                entity.setImg(R.mipmap.icon_search_key);
                mData.add(entity);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
