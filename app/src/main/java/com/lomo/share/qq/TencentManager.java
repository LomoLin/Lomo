package com.lomo.share.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lomo.share.IShare;
import com.lomo.share.ShareEntity;
import com.lomo.share.ShareListener;
import com.lomo.share.SharePlatform;
import com.lomo.share.ShareResult;
import com.lomo.share.ShareSdkConstant;
import com.lomo.share.Utils;
import com.lomo.utils.StringUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.lang.ref.SoftReference;

/**
 * Created by Administrator on 2017/4/11.
 */

public class TencentManager implements IShare {

    private SoftReference<Activity> mActivityReference;
    private Tencent mTencent;
    private ShareListener mListener;
    private QQUIListener mUIListener = new QQUIListener();

    public TencentManager(Activity activity) {
        mActivityReference = new SoftReference<Activity>(activity);
        mTencent = Tencent.createInstance(ShareSdkConstant.QQ_APP_ID, activity.getApplicationContext());
    }

    @Override
    public void share(SharePlatform.Operation operation, ShareEntity entity, ShareListener listener) {
        mListener = listener;

        switch (operation) {
            case SHARE_QQ_FRIEND:
                shareToQQ(entity);
                break;
            case SHARE_QZONE:
                shareToQZone(entity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, requestCode, data, mUIListener);
    }

    /**
     * 分享到QQ
     *
     * @param entity
     */
    private void shareToQQ(ShareEntity entity) {
        Utils.checkEntity(entity, mListener);

        final Bundle params = new Bundle();
        //通用字段
        params.putString(QQShare.SHARE_TO_QQ_TITLE, StringUtils.null2Length0(entity.getTitle()));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, StringUtils.null2Length0(entity.getContent()));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, StringUtils.null2Length0(entity.getWebUrl()));

        if (entity.getType() == ShareEntity.ShareType.IMG) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, StringUtils.null2Length0(entity.getImgUrl()));
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, StringUtils.null2Length0(entity.getImgUrl()));
        }

        mTencent.shareToQQ(mActivityReference.get(), params, mUIListener);
    }

    private void shareToQZone(ShareEntity entity) {
        Utils.checkEntity(entity, mListener);

        final Bundle bundle = new Bundle();
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, StringUtils.null2Length0(entity.getTitle()));
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, StringUtils.null2Length0(entity.getContent()));
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, StringUtils.null2Length0(entity.getWebUrl()));
        if (null != entity.getImgUrls() && entity.getImgUrls().size() > 0) {
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, entity.getImgUrls());
        }
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);

        mTencent.shareToQzone(mActivityReference.get(), bundle, mUIListener);
    }

    private class QQUIListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            mListener.onShareComplete(ShareResult.SHARE_SUCCESS, o.toString());
        }

        @Override
        public void onError(UiError uiError) {
            mListener.onShareComplete(ShareResult.SHARE_FAIL, uiError.errorCode + ":" + uiError.errorMessage + ":" + uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            mListener.onShareComplete(ShareResult.SHARE_CANCEL, "已取消");
        }
    }

}
