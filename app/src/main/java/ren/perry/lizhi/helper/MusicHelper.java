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

}
