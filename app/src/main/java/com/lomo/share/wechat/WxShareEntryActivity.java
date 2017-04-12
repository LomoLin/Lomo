package com.lomo.share.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.haomawang.base.sharesdk.ShareListener;
import com.haomawang.base.sharesdk.ShareResult;
import com.haomawang.base.sharesdk.ShareSdkConstant;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/4/12.
 */

public abstract class WxShareEntryActivity extends Activity implements IWXAPIEventHandler, ShareListener {
    private IWXAPI mWXApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWXApi = WXAPIFactory.createWXAPI(this, ShareSdkConstant.WX_APP_ID, false);
        mWXApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWXApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    onShareComplete(ShareResult.SHARE_SUCCESS, "分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    onShareComplete(ShareResult.SHARE_CANCEL, "分享取消");
                    break;
                default:
                    onShareComplete(ShareResult.SHARE_FAIL, "分享失败");
                    break;
            }
        }
    }
}
