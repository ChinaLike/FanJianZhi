package com.touchrom.fanjianzhi.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;
import com.arialyy.frame.core.MVVMFrame;
import com.arialyy.frame.http.HttpUtil;
import com.arialyy.frame.util.AndroidUtils;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.SharePreUtil;
import com.arialyy.frame.util.show.L;
import com.arialyy.frame.util.show.T;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.touchrom.fanjianzhi.activity.LauncherActivity;
import com.touchrom.fanjianzhi.activity.MainActivity;
import com.touchrom.fanjianzhi.base.BaseActivity;
import com.touchrom.fanjianzhi.base.BaseModule;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.config.ResultCode;
import com.touchrom.fanjianzhi.config.ServerConstance;
import com.touchrom.fanjianzhi.dialog.VersionUpgradeDialog;
import com.touchrom.fanjianzhi.entity.LauncherEntity;
import com.touchrom.fanjianzhi.entity.UpdateEntity;
import com.touchrom.fanjianzhi.net.ServiceUtil;
import com.touchrom.fanjianzhi.widget.GuideView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lyy on 2016/6/1.
 */
public class LauncherModule extends BaseModule {
    public LauncherModule(Context context) {
        super(context);
    }

    /**
     * 开始启动流程
     *
     * @param activity
     * @param img
     */
    public void startLauncherFlows(final LauncherActivity activity, final ImageView img) {
        Map<String, String> map = new WeakHashMap<>();
        mServiceUtil.getLauncherData(map, new HttpUtil.AbsResponse() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                try {
                    JSONObject object = new JSONObject(data);
                    if (mServiceUtil.isRequestSuccess(object)) {
                        LauncherEntity entity = new Gson().fromJson(object.getString(ServiceUtil.DATA_KEY), LauncherEntity.class);
                        launcherFlows(entity, activity, img);
                    } else {
//                        T.showShort(getContext(), mServiceUtil.getMsg(object));
                        launcherFlows(null, activity, img);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {
                super.onError(error);
                T.showShort(getContext(), "网络错误");
//                activity.finish();
                startApp();
            }
        });
    }

    /**
     * 启动流程
     */
    private void launcherFlows(final LauncherEntity entity, final LauncherActivity activity, final ImageView imageView) {
        File temp = new File(activity.getFilesDir() + Constance.PATH.GUIDE_SETTING_TEMP_PATH);
        if (temp.exists()) {
            activity.startBt();
            Observable.just(temp)
                    .map(new Func1<File, String>() {
                        @Override
                        public String call(File file) {
                            StringBuilder sb = new StringBuilder();
                            try {
                                FileInputStream fis = new FileInputStream(file);
                                byte[] buf = new byte[1024];
                                int len = 0;
                                while ((len = fis.read(buf)) > 0) {
                                    byte[] b = new byte[len];
                                    System.arraycopy(buf, 0, b, 0, len);
                                    sb.append(new String(b));
                                }
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return sb.toString();
                        }
                    })
                    .map(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String s) {
                            String code = String.valueOf(AndroidUtils.getVersionCode(activity));
                            return code.equalsIgnoreCase(s);
                        }
                    })
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {  //如果当前版本号和配置文件的版本号相同
                                startLauncherImgFlow(entity, imageView);
                            } else { //启动引导界面
                                startGuideView(activity);
                            }
                        }
                    });
        } else {
            startGuideView(activity);
        }
    }

    /**
     * 开始启动图流程
     *
     * @param entity
     * @param imageView
     */
    private void startLauncherImgFlow(final LauncherEntity entity, final ImageView imageView) {
        L.v(TAG, "开始启动图流程");
        Glide.with(getContext()).load(entity.getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 启动Main
     */
    public void startApp() {
        getContext().startActivity(new Intent(getContext(), MainActivity.class));
        MVVMFrame.getInstance().finishActivity(LauncherActivity.class);
    }

    /**
     * 打开推送，定位等配置流程
     */
    private void startOtherFlow(Context context) {
//        //启动全局服务
//        context.startService(new Intent(context, GaoShouYouService.class));
//        //预下载APP
//        if (AppManager.getInstance().getSetting().isAutoDownloadNewApp()) {
//            getUpdateInfo();
//        }
//        //通知系统扫描整个目录
//        context.sendBroadcast(new Intent(AndroidVersionUtil.hasKitKat() ? Intent.ACTION_MEDIA_SCANNER_SCAN_FILE : Intent.ACTION_MEDIA_MOUNTED,
//                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//        //打开推送
//        PushManager pm = PushManager.getInstance();
//        pm.initialize(getContext().getApplicationContext());
//        pushBind(pm.getClientid(getContext()));
    }

    /**
     * 启动引导图
     *
     * @param activity
     */
    private void startGuideView(final LauncherActivity activity) {
        callback(ResultCode.ACTIVITY.FIRST_LAUNCHER, 123);
        final String path = activity.getFilesDir() + Constance.PATH.GUIDE_SETTING_TEMP_PATH;
        File temp = new File(path);
        FileUtil.createFile(path);
        Observable.just(temp)
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.delete();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        try {
                            L.v(TAG, "启动引导界面");
                            FileUtil.createFile(path);
                            FileOutputStream fos = new FileOutputStream(path);
                            fos.write(String.valueOf(AndroidUtils.getVersionCode(activity)).getBytes());
                            fos.flush();
                            fos.close();
                            GuideView view = new GuideView(activity, activity.getSupportFragmentManager());
                            activity.getRootView().addView(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 后台下载图片
     */
    public void downloadImg(String imgUrl) {
        Observable.just(imgUrl).subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                L.v(TAG, "后台下载启动图");
                new DownLoadUtil().download(getContext(), s, Constance.PATH.LAUNCHER_IMG_PATH, new DownloadListener());
            }
        });
    }

}
