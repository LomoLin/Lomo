package com.lomo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by linjunjie on 2016/11/30.
 */

public class GlideUtil {

    public static void setRoundImage(ImageView imageView, String imgUrl, int loadingRes, int errorRes, int radius) {
        setRoundImage(true, imageView, imgUrl, loadingRes, errorRes, radius);
    }

    public static void setRoundImage(boolean isSkipMemoryCache, ImageView imageView, String imgUrl, int loadingRes, int errorRes, int radius) {
        if (null == imageView) return;

        Context context = imageView.getContext();
        isActivityFinish(context);
        Glide.with(context).load(imgUrl)
                .placeholder(loadingRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorRes)
                .skipMemoryCache(isSkipMemoryCache)
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    public static void setImage(ImageView imageView, String imgUrl, int loadingRes, int errorRes) {
        if (null == imageView) return;

        Context context = imageView.getContext();
        isActivityFinish(context);
        Glide.with(context).load(imgUrl)
                .placeholder(loadingRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorRes)
                .into(imageView);
    }

    public static void setCircleImage(ImageView imageView, String imgUrl, int length, int loadingRes, int errorRes) {
        if (null == imageView) return;

        Context context = imageView.getContext();
        isActivityFinish(context);

        Glide.with(context).load(imgUrl)
                .placeholder(loadingRes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(context))
                .override(length, length)
                .into(imageView);
    }

    private static void isActivityFinish(Context context) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) return;
        }
    }
}
