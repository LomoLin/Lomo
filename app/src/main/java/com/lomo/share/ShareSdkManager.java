package com.lomo.share;

import android.app.Activity;
import android.content.Intent;

import com.lomo.share.qq.TencentManager;
import com.lomo.share.system.SystemManager;
import com.lomo.share.wechat.WeChatManager;

import java.lang.ref.SoftReference;

/**
 * @desc
 * @autor Lomo
 * @time 2017/2/6 15:11
 */

public class ShareSdkManager implements IShare {

    private volatile static ShareSdkManager _Instance = null;
    private SoftReference<Activity> mActivitySoftReference;
    private IShare mShareManager;

    private ShareSdkManager(Activity activity) {
        mActivitySoftReference = new SoftReference<Activity>(activity);
    }

    public static ShareSdkManager getInstance(Activity activity) {
        if (_Instance == null) {
            synchronized (ShareSdkManager.class) {
                if (_Instance == null) {
                    _Instance = new ShareSdkManager(activity);
                }
            }
        }
        return _Instance;
    }

    @Override
    public void share(SharePlatform.Operation operation, ShareEntity entity, ShareListener listener) {
        mShareManager = getShareManager(operation.getPlatform());
        mShareManager.share(operation, entity, listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareManager.onActivityResult(requestCode, resultCode, data);
    }

    private IShare getShareManager(SharePlatform platform) {
        switch (platform) {
            case QQ:
                return new TencentManager(mActivitySoftReference.get());
            case WeChat:
                return new WeChatManager(mActivitySoftReference.get());
            case System:
                return new SystemManager(mActivitySoftReference.get());
            default:
                return new WeChatManager(mActivitySoftReference.get());
        }
    }
}
