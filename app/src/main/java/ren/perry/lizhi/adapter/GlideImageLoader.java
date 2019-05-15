package ren.perry.lizhi.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

import ren.perry.mvplibrary.utils.GlideMan;

/**
 * 轮播图的图片加载适配器
 *
 * @author perrywe
 * @date 2019/4/16
 * WeChat  917351143
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        String url = (String) path;
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        new GlideMan.Builder()
                .load(url)
//                .loadingRes(R.mipmap.default_rectangle)
//                .loadFailRes(R.mipmap.default_rectangle)
                .into(imageView);
    }
}