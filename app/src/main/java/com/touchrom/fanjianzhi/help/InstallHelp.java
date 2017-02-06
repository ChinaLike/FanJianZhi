package com.touchrom.fanjianzhi.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.touchrom.fanjianzhi.activity.InstallTempActivity;
import com.touchrom.fanjianzhi.config.Constance;


/**
 * Created by lyy on 2015/11/13.
 * 安装帮助类
 */
public class InstallHelp {

    /**
     * 当前使用databinding时不能直接安装APK,否则坑爹的谷歌会把你坑的不要不要的！！！
     *
     * @param activity
     */
    public static void installApk(Activity activity) {
        installApk(activity, Constance.PATH.NEW_APK_PATH);
    }

    /**
     * 安装Apk
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(context, InstallTempActivity.class);
        intent.putExtra(Constance.KEY.STRING, apkPath);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
