package com.lomo.share;

import android.content.Intent;

import com.haomawang.base.BaseActivity;
import com.haomawang.base.contact.ContactsUtils;
import com.haomawang.base.sharesdk.qq.TencentManager;
import com.haomawang.base.sharesdk.system.SystemManager;
import com.haomawang.base.sharesdk.wechat.WeChatManager;

/**
 * @desc
 * @autor ZhuangXiong
 * @time 2017/2/6 15:11
 */

public class ShareSdkManager implements IShare {

    private volatile static ShareSdkManager _Instance = null;
    private BaseActivity mActivity;
    private IShare mShareManager;

    private ShareSdkManager(BaseActivity activity) {
        this.mActivity = activity;
    }

    public static ShareSdkManager getInstance(BaseActivity activity) {
        if (_Instance == null) {
            synchronized (ContactsUtils.class) {
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
                return new TencentManager(mActivity);
            case WeChat:
                return new WeChatManager(mActivity);
            case System:
                return new SystemManager(mActivity);
            default:
                return new WeChatManager(mActivity);
        }
    }
}
