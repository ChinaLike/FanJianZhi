package com.touchrom.fanjianzhi.module;

import android.content.Context;

import com.arialyy.frame.cache.AbsCache;
import com.arialyy.frame.cache.CacheParam;
import com.arialyy.frame.cache.CacheUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.FileUtil;
import com.bumptech.glide.Glide;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.entity.sql.SearchHistoryEntity;

import java.io.File;

/**
 * Created by lyy on 2016/6/16.
 */
public class SettingModule extends BaseModule {
    public SettingModule(Context context) {
        super(context);
    }

    /**
     * 清除缓存
     */
    public void cleanCache() {
        CacheUtil util = new CacheUtil(getContext(), false);
        util.removeAll();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getContext()).clearDiskCache();
            }
        }).start();
    }

    /**
     * 删除搜索记录
     */
    public void delSearchRecord() {
        SearchHistoryEntity.deleteAll(SearchHistoryEntity.class);
    }

    /**
     * 获取缓存大小
     */
    public long getCacheSize() {
        String path = AndroidUtils.getDiskCacheDir(getContext()) + File.separator + CacheParam.DEFAULT_DIR;
        return FileUtil.getDirSize(Glide.getPhotoCacheDir(getContext()).getPath()) + FileUtil.getDirSize(path);
    }
}
