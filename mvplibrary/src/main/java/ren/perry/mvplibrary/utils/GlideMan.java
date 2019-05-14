package ren.perry.mvplibrary.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Glide图片加载框架的简单二次封装（建造者模式）
 * 可加载url,resource,file,asset,path
 * 圆形图片、圆角图片、设置圆角大小、指定图片像素大小
 * 可设置加载中图片以及失败图片
 * 请先添加以下依赖：
 * 1、compile 'com.github.bumptech.glide:glide:3.7.0'
 * 2、compile 'jp.wasabeef:glide-transformations:2.0.2'
 * <p>
 * Email: pl.w@outlook.com
 * Created by perry on 2017/9/6.
 */

@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
public class GlideMan {
    private static final int TYPE_URL = 1;
    private static final int TYPE_RES = 2;
    private static final int TYPE_FILE = 3;
    private static final int TYPE_ASSET = 4;
    private static final int TYPE_PATH = 5;

    private Context c;
    private ImageView iv;
    private int round;
    private int w;
    private int h;
    private int loadingRes;
    private int loadFailRes;

    private int resId;
    private File file;
    private String url;
    private String asset;
    private String path;

    private int currentType;
    private boolean isOverride;
    private boolean isCircle;
    private boolean isRound;
    private boolean isAnimation;

    private GlideMan(Builder b) {
        this.c = b.c;
        this.iv = b.iv;
        this.round = b.round;
        this.w = b.w;
        this.h = b.h;
        this.loadingRes = b.loadingRes;
        this.loadFailRes = b.loadFailRes;
        this.resId = b.resId;
        this.file = b.file;
        this.url = b.url;
        this.asset = b.asset;
        this.path = b.path;
        this.currentType = b.currentType;
        this.isOverride = b.isOverride;
        this.isCircle = b.isCircle;
        this.isRound = b.isRound;
        this.isAnimation = b.isAnimation;

    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Builder {
        private Context c;
        private ImageView iv;
        private int round;
        private int w;
        private int h;
        private int loadingRes;
        private int loadFailRes;

        private int resId;
        private File file;
        private String url;
        private String asset;
        private String path;

        private int currentType;
        private boolean isOverride;
        private boolean isCircle;
        private boolean isRound;
        private boolean isAnimation;

        public Builder() {
            this.currentType = 0;
            this.isOverride = false;
            this.isCircle = false;
            this.isRound = false;
            this.isAnimation = false;
        }

        /**
         * 解决第一次加载时出现图片宽高不正常的情况
         *
         * @return Builder
         */
        public Builder dotAnimation() {
            this.isAnimation = true;
            return this;
        }

        public Builder circle() {
            this.isCircle = true;
            this.isRound = false;
            return this;
        }

        public Builder round(int round) {
            this.round = round;
            this.isRound = true;
            this.isCircle = false;
            return this;
        }

        public Builder size(int w, int h) {
            this.w = w;
            this.h = h;
            this.isOverride = true;
            return this;
        }

        public Builder loadingRes(int loadingRes) {
            this.loadingRes = loadingRes;
            return this;
        }

        public Builder loadFailRes(int loadFailRes) {
            this.loadFailRes = loadFailRes;
            return this;
        }

        public Builder load(int resId) {
            this.resId = resId;
            currentType = GlideMan.TYPE_RES;
            return this;
        }

        public Builder load(File file) {
            this.file = file;
            currentType = GlideMan.TYPE_FILE;
            return this;
        }

        public Builder load(String url) {
            this.url = url;
            currentType = GlideMan.TYPE_URL;
            return this;
        }

        public Builder loadAsset(String asset) {
            this.asset = asset;
            currentType = GlideMan.TYPE_ASSET;
            return this;
        }

        public Builder loadPath(String path) {
            this.path = path;
            currentType = GlideMan.TYPE_PATH;
            return this;
        }

        public GlideMan into(ImageView iv) {
            this.c = iv.getContext();
            this.iv = iv;
            GlideMan glide = new GlideMan(this);
            glide.load();
            return glide;
        }
    }

    private void load() {
        DrawableTypeRequest request = null;
        switch (currentType) {
            case GlideMan.TYPE_URL:
                request = Glide.with(c).load(url);
                break;
            case GlideMan.TYPE_RES:
                request = Glide.with(c).load(resId);
                break;
            case GlideMan.TYPE_FILE:
                request = Glide.with(c).load(file);
                break;
            case GlideMan.TYPE_ASSET:
                request = Glide.with(c).load(asset);
                break;
            case GlideMan.TYPE_PATH:
                request = Glide.with(c).load(path);
                break;
        }
        assert request != null;
        if (isOverride) request.override(w, h);
        if (isCircle) request.bitmapTransform(new CropCircleTransformation(c));
        if (isRound) request.bitmapTransform(new RoundedCornersTransformation(c, round, 0));
        request.placeholder(loadingRes);
        request.error(loadFailRes);
        request.crossFade();
        if (isAnimation) request.dontAnimate();
        request.into(iv);
    }
}