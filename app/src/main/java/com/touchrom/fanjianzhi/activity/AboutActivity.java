package com.touchrom.fanjianzhi.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.arialyy.absadapter.listview.AbsSimpleAdapter;
import com.arialyy.absadapter.listview.AbsSimpleViewHolder;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.AndroidVersionUtil;
import com.lyy.ui.widget.NoScrollListView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.databinding.ActivityAboutBinding;
import com.touchrom.fanjianzhi.entity.WebEntity;
import com.touchrom.fanjianzhi.help.turn.TurnHelp;
import com.touchrom.fanjianzhi.util.S;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by lyy on 2016/6/16.
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {
    @InjectView(R.id.list)
    NoScrollListView mList;
    private List<AboutEntity> mData;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("关于我们");
        mData = setData();
        AbsSimpleAdapter<AboutEntity> adapter = new AbsSimpleAdapter<AboutEntity>(this, mData, R.layout.item_about) {
            @Override
            public void convert(AbsSimpleViewHolder helper, AboutEntity item) {
                helper.setText(R.id.title, item.getTitle());
                helper.setText(R.id.detail, item.getDetail());
            }
        };
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                        TurnHelp.getINSTANCE().turnSysWeb(AboutActivity.this, mData.get(position).content);
                        break;
                    case 2:
                    case 3:
                        AboutEntity entity = mData.get(position);
                        copyText(entity.getTitle(), entity.getContent());
                        S.i(mRootView, "已将QQ群号复制到剪切板了");
                        break;
                    case 4:
                        WebEntity webEntity = new WebEntity();
                        webEntity.setTitle("免责声明");
                        webEntity.setContentUrl(mData.get(position).getContent());
                        TurnHelp.getINSTANCE().turnNormalWeb(AboutActivity.this, webEntity);
                        break;
                }
            }
        });
        getBinding().setVersionName(AndroidUtils.getVersionName(this));
    }

    /**
     * 复制文字到剪切板
     */
    private void copyText(CharSequence label, CharSequence text) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (AndroidVersionUtil.hasHoneycomb()) {
            cmb.setPrimaryClip(ClipData.newPlainText(label, text));
        } else {
            S.i(mRootView, "android 3.0 以下并不支持将文字复制到剪切板");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about;
    }

    private List<AboutEntity> setData() {
        String[] title = new String[]{"官方网站", "新浪微博", "贱友QQ壹群", "贱友QQ贰群", "免责声明"};
        String[] detail = new String[]{"www.fanjian.net", "@犯贱志", "477476620", "557914990", " "};
        String[] content = new String[]{
                Constance.URL.OFFICIAL, Constance.URL.WEIBO, "477476620", "557914990", Constance.URL.DISCLAIMER
        };
        List<AboutEntity> list = new ArrayList<>();
        for (int i = 0, count = title.length; i < count; i++) {
            AboutEntity entity = new AboutEntity(title[i], detail[i], content[i]);
            list.add(entity);
        }
        return list;
    }

    private class AboutEntity {
        String title;
        String detail;
        String content;

        public AboutEntity(String title, String detail, String content) {
            this.title = title;
            this.detail = detail;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
