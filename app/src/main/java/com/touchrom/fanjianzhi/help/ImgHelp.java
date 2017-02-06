package com.touchrom.fanjianzhi.help;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.touchrom.fanjianzhi.R;

/**
 * Created by lyy on 2016/6/7.
 */
public class ImgHelp {

    /**
     * 设置图片
     *
     * @param context
     * @param imgUrl
     * @param imageView
     */
    public static void setImg(Context context, String imgUrl, ImageView imageView) {
        setImg(context, imgUrl, R.mipmap.icon_def_icon, imageView);
    }

    /**
     * 设置图片
     *
     * @param context
     * @param imgUrl
     * @param drawableRes
     * @param imageView
     */
    public static void setImg(Context context, String imgUrl, @DrawableRes int drawableRes, ImageView imageView) {
        Glide.with(context).load(imgUrl)
//                .placeholder(drawableRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
//                .fitCenter()
                .error(drawableRes)
                .into(imageView);
    }

//    public static AnimationDrawable createLoadingImg(Context context){
//        AnimationDrawable ad = new AnimationDrawable();
//        ad.addFrame(context.getResources().getDrawable(R.mipmap.loading_1), 100);
//        ad.addFrame(context.getResources().getDrawable(R.mipmap.loading_2), 100);
//        ad.addFrame(context.getResources().getDrawable(R.mipmap.loading_3), 100);
//        ad.addFrame(context.getResources().getDrawable(R.mipmap.loading_4), 100);
//        ad.addFrame(context.getResources().getDrawable(R.mipmap.loading_5), 100);
//        ad.setOneShot(false);
//        return ad;
//    }
}
