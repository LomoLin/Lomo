package com.lomo.share;

/**
 * Created by Administrator on 2017/4/11.
 */

public enum SharePlatform {
    QQ("QQ"),WeChat("WeChat"),System("System");

    private String paltform;

    SharePlatform(String paltform) {
        this.paltform = paltform;
    }

    public String getPaltform() {
        return paltform;
    }

    public enum Operation {
        SHARE_QQ_FRIEND("QQ好友") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.QQ;
            }
        },
        SHARE_QZONE("QQ空间") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.QQ;
            }
        },
        SHARE_WECHAT_FRIEND("微信好友") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.WeChat;
            }
        },
        SHARE_WECHAT_TIMELINE("微信朋友圈") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.WeChat;
            }
        },
        SHARE_WECHAT_FAVORITE("微信收藏") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.WeChat;
            }
        },
        SHARE_SMS("短信") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.System;
            }
        },
        SHARE_EMAIL("邮件") {
            @Override
            public SharePlatform getPlatform() {
                return SharePlatform.System;
            }
        };

        private String operation;

        Operation(String operation) {
            this.operation = operation;
        }

        public String getOperation() {
            return operation;
        }

        public abstract SharePlatform getPlatform();
    }
}
