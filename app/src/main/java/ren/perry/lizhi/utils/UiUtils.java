package ren.perry.lizhi.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ren.perry.lizhi.R;
import ren.perry.lizhi.app.MyApp;

public class UiUtils {

    //屏幕缩放比率
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;
    public static final float SCALE_X;
    public static final float SCALE_Y;

    static {
        Resources resources = MyApp.getContext().getResources();
        SCREEN_WIDTH = resources.getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = resources.getDisplayMetrics().heightPixels;
        SCALE_X = SCREEN_WIDTH / 640.0f;
        SCALE_Y = SCREEN_HEIGHT / 1136.0f;
    }

    public static int getColor(int id) {
        return MyApp.getContext().getResources().getColor(id);
    }

    public static int getAppColor() {
        return getColor(R.color.app_color);
    }

    @NonNull
    public static String getString(int id) {
        return MyApp.getContext().getResources().getString(id);
    }

    @NonNull
    public static String[] getStringArray(int id) {
        return MyApp.getContext().getResources().getStringArray(id);
    }

    @NonNull
    public static int[] getIntArray(int id) {
        return MyApp.getContext().getResources().getIntArray(id);
    }

    public static int getDimensionPixelSize(int id) {
        return MyApp.getContext().getResources().getDimensionPixelSize(id);
    }

    public static View getView(int id) {
        return LayoutInflater.from(MyApp.getContext()).inflate(id, null);
    }

    public static Drawable getDrawable(int id) {
        return MyApp.getContext().getResources().getDrawable(id);
    }

    public static Animation getAnimation(int id) {
        return AnimationUtils.loadAnimation(MyApp.getContext(), id);
    }

    public static void setMargin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变（DisplayMetrics类中属性density）
     */
    public static int px2dp(float pxValue) {
        final float scale = MyApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变（DisplayMetrics类中属性density）
     */
    public static int dp2px(float dpValue) {
        final float scale = MyApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变（DisplayMetrics类中属性scaledDensity）
     */
    public static int px2sp(float pxValue) {
        final float fontScale = MyApp.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变（DisplayMetrics类中属性scaledDensity）
     */
    public static int sp2px(float spValue) {
        final float fontScale = MyApp.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}