package com.touchrom.fanjianzhi.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.arialyy.downloadutil.DownLoadUtil;
import com.arialyy.downloadutil.DownloadListener;
import com.arialyy.frame.util.FileUtil;
import com.arialyy.frame.util.StringUtil;
import com.arialyy.frame.util.show.FL;
import com.arialyy.frame.util.show.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.lyy.ui.imagezoom.ImageViewTouch;
import com.lyy.ui.imagezoom.ImageViewTouchBase;
import com.lyy.ui.large_img.ImageSource;
import com.lyy.ui.large_img.SubsamplingScaleImageView;
import com.touchrom.fanjianzhi.R;
import com.touchrom.fanjianzhi.base.BaseFragment;
import com.touchrom.fanjianzhi.config.Constance;
import com.touchrom.fanjianzhi.databinding.FragmentGuideBinding;
import com.touchrom.fanjianzhi.dialog.LoadingDialog;
import com.touchrom.fanjianzhi.entity.sql.LargeImgInfoEntity;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class ImgBrowseFragment extends BaseFragment<FragmentGuideBinding> {
    private static final String EXTENSION = "_small";
    @InjectView(R.id.img)
//    ImageViewTouch mImg;
            ImageView mImg;
    @InjectView(R.id.lager_img)
    SubsamplingScaleImageView mLagerImg;
    private String mUrl;
    int LOAD_URL = 0;
    int LOAD_FILE = 1;
    private DownLoadUtil mUtil;

    private ImgBrowseFragment(String url) {
        mUrl = url;
//        mUtil = new DownLoadUtil();
    }

    public static ImgBrowseFragment newInstance(String url) {
        return new ImgBrowseFragment(url);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_img_browse;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

    }

    @Override
    protected void onDelayLoad() {
        super.onDelayLoad();
        if (!TextUtils.isEmpty(mUrl)) {
            fun2();
        }
    }

    public ImgBrowseFragment get(View view) {
        return (ImgBrowseFragment) view.getTag(mLagerImg.getId());
    }

    private void fun2() {
        int endP = mUrl.lastIndexOf(".");
        String name = endP > -1 ? mUrl.substring(endP + 1, mUrl.length()) : "";
        boolean isGif = name.equals("gif");
        if (!isGif) {
            mLagerImg.setVisibility(View.VISIBLE);
            showLoadingDialog();
            mLagerImg.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener() {
                @Override
                public void onImageLoaded() {
                    super.onImageLoaded();
                    dismissLoadingDialog();
                }
            });

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    getBitmap();
//                }
//            }).start();
            handleLargeImg(mUrl, Constance.PATH.TEMP + StringUtil.keyToHashKey(mUrl));
        } else {
            mImg.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mImg);
        }
    }


    private void getBitmap() {
        new Downloader(Glide.with(getContext())).execute(mUrl);
//        Glide.with(getContext()).load(mUrl).downloadOnly(new BaseTarget<File>() {
//            @Override
//            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                super.onLoadFailed(e, errorDrawable);
//                L.e(TAG, FL.getExceptionString(e));
//            }
//
//            @Override
//            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
//                L.d(TAG, "SUCCESS");
//            }
//
//            @Override
//            public void getSize(SizeReadyCallback cb) {
//
//            }
//        });
    }

    class Downloader extends AsyncTask<String, File, Downloader.Result> {
        private static final String TAG = "Downloader";
        private final RequestManager glide;

        public Downloader(RequestManager glide) {
            this.glide = glide;
        }

        @Override
        protected Result doInBackground(String... params) {
            @SuppressWarnings("unchecked")
            FutureTarget<File>[] requests = new FutureTarget[params.length];
            // fire everything into Glide queue
            for (int i = 0; i < params.length; i++) {
                if (isCancelled()) {
                    break;
                }
                requests[i] = glide
                        .load(params[i])
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
            }
            // wait for each item
            Result result = new Result();
            for (int i = 0; i < params.length; i++) {
                if (isCancelled()) {
                    for (int j = i; j < params.length; j++) {
                        if (requests[i] != null) Glide.clear(requests[i]);
                        result.failures.put(params[j], new CancellationException());
                    }
                    break;
                }
                try {
                    File file = requests[i].get(1000, TimeUnit.SECONDS);
                    result.success.put(params[i], file);
                    publishProgress(file);
                } catch (Exception e) {
                    L.e(TAG, FL.getExceptionString(e));
                    result.failures.put(params[i], e);
                } finally {
                    Glide.clear(requests[i]);
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(File... values) {
            for (File file : values) {
                Log.v(TAG, "Finished " + file);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            Log.i(TAG, String.format(Locale.ROOT, "Downloaded %d files, %d failed.",
                    result.success.size(), result.failures.size()));
            File temp = result.success.get(mUrl);
            if (temp != null) {
                mHandleImg.obtainMessage(LOAD_FILE, temp.getPath()).sendToTarget();
            }
        }

        class Result {
            Map<String, File> success = new HashMap<>();
            Map<String, Exception> failures = new HashMap<>();
        }
    }

    @Override
    public void onDestroy() {
        if (mUtil != null) {
            mUtil.stopDownload();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (mUtil != null) {
            mUtil.stopDownload();
        }
        super.onDestroyView();
    }

    private void fun1() {
//        if (!mUrl.contains(EXTENSION)) {
//            mImg.setVisibility(View.VISIBLE);
//            mLagerImg.setVisibility(View.GONE);
//            Glide.with(getContext()).load(mUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            mImg.setLayerType(resource.getIntrinsicHeight() > 4096 ? View.LAYER_TYPE_SOFTWARE :
//                                    View.LAYER_TYPE_HARDWARE, null);
//                            return false;
//                        }
//                    })
//                    .into(mImg);
//            mImg.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
//            mImg.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (mImg.getScale() > 1f) {
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                    } else {
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                    return false;
//                }
//            });
//        } else {
//            showLoadingDialog();
//            mLagerImg.setVisibility(View.VISIBLE);
//            mImg.setVisibility(View.GONE);
//            mLagerImg.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener() {
//                @Override
//                public void onImageLoaded() {
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            dismissLoadingDialog();
//                        }
//                    });
//                }
//            });
//            int end = mUrl.indexOf(EXTENSION);
//            String name = FileUtil.getFileExtensionName(mUrl);
//            String url = mUrl.substring(0, end) + "." + name;
//            handleLargeImg(url, Constance.PATH.TEMP + StringUtil.keyToHashKey(url));
//        }
    }

    private void test() {
        mLagerImg.setVisibility(View.VISIBLE);
        mLagerImg.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener() {
            @Override
            public void onImageLoaded() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                    }
                });
            }
        });
        handleLargeImg(mUrl, Constance.PATH.TEMP + StringUtil.keyToHashKey(mUrl));
    }


    private void handleLargeImg(final String url, final String path) {
        LargeImgInfoEntity temp = DataSupport.where("imgUrl = ?", url).findFirst(LargeImgInfoEntity.class);
//        final DownLoadUtil util = new DownLoadUtil();
        mUtil = new DownLoadUtil();
        if (temp != null) {
            if (!temp.isCompleted()) {
                temp.delete();
            } else {
                mHandleImg.obtainMessage(LOAD_FILE, path).sendToTarget();
                return;
            }
        }
        final LargeImgInfoEntity entity = new LargeImgInfoEntity();
        mUtil.download(getContext(), url, path, new DownloadListener() {
            @Override
            public void onPreDownload(HttpURLConnection connection) {
                entity.setImgUrl(url);
                entity.setLen(connection.getContentLength());
                saveEntity(entity, url);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                entity.setTime(System.currentTimeMillis());
                entity.setCompleted(true);
                saveEntity(entity, url);
                mHandleImg.obtainMessage(LOAD_FILE, path).sendToTarget();
            }

        });
    }

    private void saveEntity(LargeImgInfoEntity entity, String url) {
//        entity.saveIfNotExist("imgUrl = ?", url);
        entity.save();
    }

    private Handler mHandleImg = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_FILE) {
                handleDownloadedImg(msg.obj + "");
            } else {
                handleBitmap((Bitmap) msg.obj);
            }
        }

        private void handleDownloadedImg(final String path) {
            mLagerImg.setImage(ImageSource.uri(path));
        }

        private void handleBitmap(Bitmap bm) {
            mLagerImg.setImage(ImageSource.bitmap(bm));
        }
    };
}