package ren.perry.lizhi.helper;

import ren.perry.lizhi.app.MyApp;
import ren.perry.mvplibrary.utils.SpUtils;

/**
 * 启动图数据
 *
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class SplashHelper {
    private SpUtils spUtils;

    private final String SP_KEY_SPLASH_URL = "splash_url";

    public static SplashHelper getInstance() {
        return new SplashHelper();
    }

    private SplashHelper() {
        spUtils = new SpUtils(MyApp.getContext(), "sp_splash");
    }

    /**
     * 保存启动图Url
     *
     * @param url url
     */
    public void saveUrl(String url) {
        spUtils.putString(SP_KEY_SPLASH_URL, url);
    }

    /**
     * 从缓存中获取图片Url
     *
     * @return url
     */
    public String getUrl() {
        return spUtils.getString(SP_KEY_SPLASH_URL, "");
    }
}
