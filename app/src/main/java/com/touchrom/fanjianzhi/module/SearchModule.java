package com.touchrom.fanjianzhi.module;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.show.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.entity.MainContentListEntity;
import com.touchrom.fanjianzhi.entity.SearchAdapterEntity;
import com.touchrom.fanjianzhi.entity.d_entity.ImgTextEntity;
import com.touchrom.fanjianzhi.entity.sql.SearchHistoryEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lyy on 2016/6/6.
 * 搜索模型
 */
public class SearchModule extends BaseModule {
    public SearchModule(Context context) {
        super(context);
    }

    /**
     * 搜索内容
     */
    public void searchContent(final String key, int page) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("text", key);
        params.put("limit", 10 + "");
        params.put("page", page + "");
        mServiceUtil.searchContent(params, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<MainContentListEntity> lists;
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        lists = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<MainContentListEntity>>() {
                                }.getType());
//                        tintList(lists, key);
                        callback(ResultCode.SERVER.SEARCH_CONTENT, lists);
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                callback(ResultCode.SERVER.SEARCH_CONTENT, ServiceUtil.ERROR);
            }
        });
    }

//    private void tintList(List<MainContentListEntity> lists, String key) {
//        for (MainContentListEntity entity : lists) {
//            ImgTextEntity relayEntity = null;
//            if (entity.getImgTextContent() != null) {
//                relayEntity = entity.getImgTextContent();
//            } else if (entity.getVideoContent() != null) {
//                relayEntity = entity.getVideoContent();
//            } else if (entity.getImgContent() != null) {
//                relayEntity = entity.getImgContent();
//            } else if (entity.getTextContent() != null) {
//                relayEntity = entity.getTextContent();
//            }
//
//            if (relayEntity != null) {
//                relayEntity.setTitle(tintKey(relayEntity.getTitle().toString(), key));
//                String detail = relayEntity.getDetail().toString();
//                if (TextUtils.isEmpty(detail)) {
//                    continue;
//                }
//                relayEntity.setDetail(tintKey(detail, key));
//            }
//        }
//        callback(ResultCode.SERVER.SEARCH_CONTENT, lists);
//    }

    /**
     * 获取搜索记录
     */
    public List<CharSequence> getSearchHistory() {
        List<SearchHistoryEntity> datas = SearchHistoryEntity.select("title")
                .order("time desc").limit(5).find(SearchHistoryEntity.class);
        List<CharSequence> realData = new ArrayList<>();
        if (datas != null && datas.size() > 0) {
            for (SearchHistoryEntity entity : datas) {
                realData.add(entity.getTitle());
            }
        }
        callback(ResultCode.SERVER.SEARCH_HISTORY, realData);
        return realData;
    }


    /**
     * 从服务器获取搜索数据
     */
    public void getNetSearchData(final String key) {
        Map<String, String> params = new WeakHashMap<>();
        params.put("text", key);
        params.put("page", "1");
        params.put("limit", "5");
        mServiceUtil.searchKey(params, new HttpUtil.AbsResponse() {

            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                List<String> keys;
                List<CharSequence> realData = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(obj)) {
                        keys = new Gson().fromJson(obj.getJSONArray(ServiceUtil.DATA_KEY).toString()
                                , new TypeToken<List<String>>() {
                                }.getType());
                        for (String str : keys) {
                            if (str.contains(key)) {
                                realData.add(tintKey(str, key));
                            }
                        }
                    } else {
                        T.showShort(getContext(), mServiceUtil.getMsg(obj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    callback(ResultCode.SERVER.GET_SEARCH_KEY_DATA, realData);
                }
            }

        });
    }

    /**
     * 储存搜索数据
     */
    public void saveSearchData(SearchHistoryEntity search) {
        if (!DataSupport.isExist(SearchHistoryEntity.class, "title = ?", search.getTitle())) {
            search.save();
        }
    }

    private synchronized CharSequence tintKey(String str, String key) {
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
