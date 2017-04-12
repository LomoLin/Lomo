package com.lomo.share.system;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.lomo.share.IShare;
import com.lomo.share.ShareEntity;
import com.lomo.share.ShareListener;
import com.lomo.share.SharePlatform;
import com.lomo.share.ShareResult;
import com.lomo.share.Utils;
import com.lomo.utils.StringUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/4/12.
 */

public class SystemManager implements IShare {
    public static final int SHARE_SMS_REQUEST_CODE = 1002;
    public static final int SHARE_EMAIL_REQUEST_CODE = 1001;

    private Activity mActivity;
    private ShareListener mListener;

    public SystemManager(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void share(SharePlatform.Operation operation, ShareEntity entity, ShareListener listener) {
        mListener = listener;

        switch (operation) {
            case SHARE_EMAIL:
                shareToEmail(entity);
                break;
            case SHARE_SMS:
                shareToSMS(entity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHARE_SMS_REQUEST_CODE || requestCode == SHARE_EMAIL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mListener.onShareComplete(ShareResult.SHARE_SUCCESS, "分享成功");
            } else if (requestCode == Activity.RESULT_CANCELED) {
                mListener.onShareComplete(ShareResult.SHARE_CANCEL, "分享取消");
            }
        }
    }

    /**
     * 短信分享
     *
     * @param entity
     */
    private void shareToSMS(ShareEntity entity) {
        Utils.checkEntity(entity, mListener);

        Uri smsUri = Uri.parse("smsto:");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, smsUri);
        smsIntent.putExtra("sms_body", entity.getContent() + StringUtils.null2Length0(entity.getWebUrl()));
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        smsIntent.setType("image/*");
        smsIntent.setType("vnd.android-dir/mms-sms");
        if (!TextUtils.isEmpty(entity.getImgUrl())) {
            smsIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(entity.getImgUrl())));
        }

        mActivity.startActivityForResult(smsIntent, SHARE_SMS_REQUEST_CODE);
    }

    /**
     * 邮件分享
     *
     * @param entity
     */
    private void shareToEmail(ShareEntity entity) {
        Utils.checkEntity(entity, mListener);

        Uri emailUri = Uri.parse("mailto:");
        Intent emailIntent = new Intent(Intent.ACTION_SEND, emailUri);
        emailIntent.setType("image/*");
        emailIntent.setType("message/rfc882");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, StringUtils.null2Length0(entity.getTitle()));
        emailIntent.putExtra(Intent.EXTRA_TEXT, entity.getContent());
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mActivity.startActivityForResult(emailIntent, SHARE_EMAIL_REQUEST_CODE);
    }
}
