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

    public void enableAutoPlay(boolean isAuto) {
        spUtils.putBoolean("auto_play", isAuto);
    }

    public boolean isAutoPlay() {
        return spUtils.getBoolean("auto_play", false);
    }


}
