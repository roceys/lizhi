package ren.perry.lizhi.app;

import ren.perry.lizhi.BuildConfig;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public interface Constants {

    interface App {
        boolean isDebug = BuildConfig.DEBUG;
        String appId = BuildConfig.APPLICATION_ID;
        int versionCode = BuildConfig.VERSION_CODE;
    }

    interface Api {
        String url = BuildConfig.SERVER_URL;
        String module = BuildConfig.SERVER_MODULE;
    }
}
