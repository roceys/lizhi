package ren.perry.lizhi.helper;

import java.util.List;

import ren.perry.lizhi.dao.DaoManager;
import ren.perry.lizhi.dao.MusicDao;
import ren.perry.lizhi.entity.Music;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class AudioPlayer {
    private volatile static AudioPlayer player;

    public static final int TYPE_ADD_SUCCESS = 1;   //添加成功
    public static final int TYPE_ADD_FAIL = 2;      //添加失败
    public static final int TYPE_ADD_EXISTED = 3;   //已存在

    private List<Music> musicList;

    private AudioPlayer() {

    }

    public static AudioPlayer get() {
        if (player == null) {
            synchronized (AudioPlayer.class) {
                if (player == null) {
                    player = new AudioPlayer();
                }
            }
        }
        return player;
    }

    public void init() {
        musicList = DaoManager.get()
                .getDaoSession()
                .getMusicDao()
                .queryBuilder()
                .orderDesc(MusicDao.Properties.Add_time)
                .list();
    }

//    /**
//     * 添加音乐到播放列表
//     *
//     * @param music 音乐
//     * @return
//     */
//    public int add(Music music) {
//        if (musicList.contains(music)) {
//            return TYPE_ADD_EXISTED;
//        }

//    }


}
