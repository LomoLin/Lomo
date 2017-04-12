package com.lomo.share;

import android.content.Intent;

/**
 * Created by Administrator on 2017/4/11.
 */

public interface IShare {

    void share(SharePlatform.Operation operation, ShareEntity entity, ShareListener listener);

    //QQ分享宿主Activity需要执行原生回调onActivityResult,并在onActivityResult中执行该方法
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
