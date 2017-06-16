package com.lomo.share.dialog;

import android.app.Activity;
import android.view.View;

import com.kelin.mvvmlight.base.ViewModel;
import com.lomo.share.ShareEntity;
import com.lomo.share.ShareListener;
import com.lomo.share.SharePlatform;
import com.lomo.share.ShareSdkManager;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ShareDialogViewModel implements ViewModel {

    private Activity mActivity;
    private ShareDialog mShareDialog;
    private ShareEntity mShareEntity;
    private ShareListener mShareListener;

    public ShareDialogViewModel(Activity activity,
                                ShareDialog shareDialog,
                                ShareEntity shareEntity,
                                ShareListener shareListener) {
        mActivity = activity;
        mShareDialog = shareDialog;
        mShareEntity = shareEntity;
        mShareListener = shareListener;
    }

    /**
     * 分享到qq好友
     *
     * @param view
     */
    public void shareQQFriend(View view) {
        ShareSdkManager.getInstance(mActivity).share(
                SharePlatform.Operation.SHARE_QQ_FRIEND,
                mShareEntity,
                mShareListener);

        setDialogDismiss();
    }

    /**
     * 分享到微信好友
     *
     * @param view
     */
    public void shareWechatFriend(View view) {
        ShareSdkManager.getInstance(mActivity).share(
                SharePlatform.Operation.SHARE_WECHAT_FRIEND,
                mShareEntity,
                mShareListener);

        setDialogDismiss();
    }

    /**
     * 分享到微信朋友圈
     *
     * @param view
     */
    public void shareWechatTimeline(View view) {
        ShareSdkManager.getInstance(mActivity).share(
                SharePlatform.Operation.SHARE_WECHAT_TIMELINE,
                mShareEntity,
                mShareListener);

        setDialogDismiss();
    }

    private void setDialogDismiss() {
        mShareDialog.dismiss();
//        mShareDialog = null;
    }
}
