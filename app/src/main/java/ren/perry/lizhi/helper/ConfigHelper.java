package ren.perry.lizhi.helper;

import ren.perry.lizhi.app.MyApp;
import ren.perry.mvplibrary.utils.SpUtils;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class ConfigHelper {

    public static volatile ConfigHelper helper;

    private static final String KEY_ABOUT_CONTENT = "key_about_content";

    private SpUtils spUtils;

    public static ConfigHelper getInstance() {
        if (helper == null) {
            synchronized (ConfigHelper.class) {
                if (helper == null) {
                    helper = new ConfigHelper();
                }
            }
        }
        return helper;
    }

    private ConfigHelper() {
        spUtils = new SpUtils(MyApp.getContext(), "config_helper");
    }

    public void saveAbout(String content) {
        spUtils.putString(KEY_ABOUT_CONTENT, content);
    }

    public String getAbout() {
        return spUtils.getString(KEY_ABOUT_CONTENT, "");
    }


}
