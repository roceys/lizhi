package ren.perry.lizhi.helper;

import ren.perry.lizhi.app.MyApp;
import ren.perry.mvplibrary.utils.SpUtils;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class MusicHelper {

    private static final String KEY_POSITION = "key_position";
    private static final String KEY_LOOP_MODE = "key_looper_mode";
    private static final String KEY_DURATION_CURRENT = "key_duration_current";
    private static final String KEY_DURATION_TOTAL = "key_duration_total";


    public static final int LOOP_MODE_SINGLE = 1;
    public static final int LOOP_MODE_LIST_LOOP = 2;

    private volatile static MusicHelper helper;

    private SpUtils spUtils;

    private MusicHelper() {
        spUtils = new SpUtils(MyApp.getContext(), "music_helper");
    }

    public static MusicHelper getInstance() {
        if (helper == null) {
            synchronized (MusicHelper.class) {
                if (helper == null) {
                    helper = new MusicHelper();
                }
            }
        }
        return helper;
    }

    public void savePosition(int position) {
        spUtils.putInt(KEY_POSITION, position);
    }


    public int getPosition() {
        return spUtils.getInt(KEY_POSITION, 0);
    }

    public void saveLoopMode(int mode) {
        spUtils.putInt(KEY_LOOP_MODE, mode);
    }

    public int getLoopMode() {
        return spUtils.getInt(KEY_LOOP_MODE, LOOP_MODE_LIST_LOOP);
    }

    public void saveCurrentDuration(int duration) {
        spUtils.putInt(KEY_DURATION_CURRENT, duration);
    }

    public int getCurrentDuration() {
        return spUtils.getInt(KEY_DURATION_CURRENT, 0);
    }

    public void saveTotalDuration(int duration) {
        spUtils.putInt(KEY_DURATION_TOTAL, duration);
    }

    public int getTotalDuration() {
        return spUtils.getInt(KEY_DURATION_TOTAL, 0);
    }

}
