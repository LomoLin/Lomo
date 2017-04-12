package com.lomo.share;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/11.
 */

public class ShareEntity {
    //标题
    private String title;
    //内容
    private String content;
    //图片url，微信传图片请使用bitmap字段
    private String imgUrl;
    //网页url
    private String webUrl;
    //多张图片,该字段QZone分享不能为空
    private ArrayList<String> imgUrls;
    //分享类型
    public enum ShareType {
        TEXT("text"),//文本
        IMG("image"),//本地图片
        WEB("web");//网页

        private String type;

        ShareType(String type) {
            this.type = type;
        }

        public String getName() {
            return type;
        }
    }

    private ShareType type;
    //微信分享传图片请使用这个字段
    private Bitmap bitmap;

    private ShareEntity(EntityBuilder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.imgUrl = builder.imgUrl;
        this.webUrl = builder.webUrl;
        this.type = builder.type;
        this.imgUrls = builder.imgUrls;
        this.bitmap = builder.bitmap;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public ShareType getType() {
        return type;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public static class EntityBuilder {
        //标题
        private String title;
        //内容
        private String content;
        //图片url
        private String imgUrl;
        //网页url
        private String webUrl;
        //分享类型
        private ShareType type;
        //多张图片
        private ArrayList<String> imgUrls;
        //单张图片,微信使用该方法
        private Bitmap bitmap;

        /**
         * 构造器
         *
         * @param type
         * @param content
         */
        public EntityBuilder(ShareType type, String content) {
            this.type = type;
            this.content = content;
        }

        public EntityBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EntityBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public EntityBuilder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public EntityBuilder setWebUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public EntityBuilder setShareType(ShareType type) {
            this.type = type;
            return this;
        }

        public EntityBuilder setImgUrls(ArrayList<String> imgUrls) {
            this.imgUrls = imgUrls;
            return this;
        }

        public EntityBuilder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public ShareEntity build() {
            return new ShareEntity(this);
        }
    }
}
