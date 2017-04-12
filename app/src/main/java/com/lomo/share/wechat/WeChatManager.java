package com.lomo.share.wechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.lomo.share.IShare;
import com.lomo.share.ShareEntity;
import com.lomo.share.ShareListener;
import com.lomo.share.SharePlatform;
import com.lomo.share.ShareResult;
import com.lomo.share.ShareSdkConstant;
import com.lomo.share.Utils;
import com.lomo.utils.StringUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.SoftReference;


/**
 * Created by Administrator on 2017/4/12.
 */

public class WeChatManager implements IShare {

    private static final int THUMB_SIZE = 150;

    private SoftReference<Activity> mActivitySoftReference;
    private IWXAPI api;
    private ShareListener mListener;

    public WeChatManager(Activity activity) {
        mActivitySoftReference = new SoftReference<Activity>(activity);
        api = WXAPIFactory.createWXAPI(activity.getApplicationContext(), ShareSdkConstant.WX_APP_ID, true);
        api.registerApp(ShareSdkConstant.WX_APP_ID);
    }


    @Override
    public void share(SharePlatform.Operation operation, ShareEntity entity, ShareListener listener) {
        mListener = listener;

        switch (operation) {
            case SHARE_WECHAT_FRIEND:
                shareToFriend(entity);
                break;
            case SHARE_WECHAT_TIMELINE:
                shareToTimeline(entity);
                break;
            case SHARE_WECHAT_FAVORITE:
                shareToFavorite(entity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 分享给微信好友
     *
     * @param entity
     */
    private void shareToFriend(ShareEntity entity) {
        chooseShareMethod(entity, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享到朋友圈
     *
     * @param entity
     */
    private void shareToTimeline(ShareEntity entity) {
        chooseShareMethod(entity, SendMessageToWX.Req.WXSceneTimeline);
    }

    /**
     * 微信收藏
     *
     * @param entity
     */
    private void shareToFavorite(ShareEntity entity) {
        chooseShareMethod(entity, SendMessageToWX.Req.WXSceneFavorite);
    }

    /**
     * 分享文本
     * <p>
     * scene取值
     * WXSceneSession:分享到会话
     * WXSceneTimeline:分享到朋友圈
     * WXSceneFavorite:微信收藏
     *
     * @param entity
     */
    private void shareText(ShareEntity entity, int scene) {
        checkParam(entity.getContent(), "分享的内容不能为空");

        //设置参数
        WXTextObject textObj = new WXTextObject();
        textObj.text = entity.getContent();

        WXMediaMessage mediaMsg = new WXMediaMessage();
        mediaMsg.mediaObject = textObj;
        mediaMsg.description = entity.getContent();

        //Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(entity.getType().getName());
        req.message = mediaMsg;
        req.scene = scene;

        api.sendReq(req);
    }

    /**
     * 分享图片
     *
     * @param entity
     */
    private void shareImage(final ShareEntity entity, final int scene) {
        if (null == entity.getBitmap()) {
            mListener.onShareComplete(ShareResult.SHARE_FAIL, "图片地址不能为空");
            return;
        }

        final String imgUrl = entity.getImgUrl();
        Bitmap bitmap = entity.getBitmap();
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();

        //设置参数
        WXImageObject imgObj = new WXImageObject();
        imgObj.imagePath = imgUrl;

        WXMediaMessage mediaMsg = new WXMediaMessage();
        mediaMsg.mediaObject = imgObj;
        mediaMsg.thumbData = Utils.bmpToByteArray(thumbBmp, true);

        //Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(entity.getType().getName());
        req.message = mediaMsg;
        req.scene = scene;

        api.sendReq(req);
    }

    /**
     * 分享网页
     *
     * @param entity
     */
    private void shareWebPage(final ShareEntity entity, final int scene) {
        checkParam(entity.getWebUrl(), "目标网页地址不能为空");

        Bitmap bitmap = entity.getBitmap();
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();

        WXWebpageObject webObj = new WXWebpageObject();
        webObj.webpageUrl = entity.getWebUrl();

        WXMediaMessage mediaMsg = new WXMediaMessage();
        mediaMsg.title = StringUtils.null2Length0(entity.getTitle());
        mediaMsg.description = entity.getContent();
        mediaMsg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
        mediaMsg.mediaObject = webObj;

        //Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(entity.getType().getName());
        req.message = mediaMsg;
        req.scene = scene;

        api.sendReq(req);
    }

    /**
     * 获取transaction唯一值
     *
     * @param type
     * @return
     */
    private String buildTransaction(String type) {
        return (null == type) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 检查参数
     *
     * @param param
     * @param msg
     */
    private void checkParam(String param, String msg) {
        if (TextUtils.isEmpty(param)) {
            mListener.onShareComplete(ShareResult.SHARE_FAIL, msg);
            return;
        }
    }

    private void chooseShareMethod(ShareEntity entity, int scene) {
        switch (entity.getType()) {
            case TEXT:
                shareText(entity, scene);
                break;
            case IMG:
                shareImage(entity, scene);
                break;
            case WEB:
                shareWebPage(entity, scene);
                break;
            default:
                break;
        }
    }
}
