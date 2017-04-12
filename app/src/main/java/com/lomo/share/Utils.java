package com.lomo.share;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/4/12.
 */

public class Utils {

    /**
     * 保证分享的数据不为空
     *
     * @param entity
     * @param listener
     */
    public static void checkEntity(ShareEntity entity, ShareListener listener) {
        if (null == entity) {
            listener.onShareComplete(ShareResult.SHARE_FAIL, "分享数据不能为空");
            return;
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
