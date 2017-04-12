package com.lomo.share;

/**
 * Created by Administrator on 2017/4/11.
 */

public enum ShareResult {
    SHARE_SUCCESS("Success"),SHARE_FAIL("Fail"),SHARE_CANCEL("Cancel");

    private String state;

    ShareResult(String state) {
        this.state = state;
    }
}
