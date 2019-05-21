package ren.perry.lizhi.helper;

import android.content.Context;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ren.perry.lizhi.dao.DaoManager;
import ren.perry.lizhi.dao.MusicDao;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.event.PlayActionEvent;


/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class AudioPlayer {
    private MusicDao dao;
    private List<Music> musicList;

    private Context context;

    private AudioPlayer(Context context) {
        this.context = context;
        dao = DaoManager.get().getDaoSession().getMusicDao();
        musicList = dao.queryBuilder().build().list();
    }

    public static AudioPlayer get(Context context) {
        return new AudioPlayer(context);
    }

    private void toastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void savePlayPosition(int position) {
        MusicHelper.getInstance().savePosition(position);
    }

    public int getPlayPosition() {
        int position = MusicHelper.getInstance().getPosition();
        if (position < 0 || position >= musicList.size()) {
            position = 0;
            savePlayPosition(position);
        }
        return position;
    }

    public Music getPlayMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        return musicList.get(getPlayPosition());
    }

    public Music getNextMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        int position = getPlayPosition() + 1;
        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }
        return musicList.get(position);
    }

    private void play(int position, int action) {
        if (musicList.isEmpty()) {
            toastShow("当前播放列表没有音乐");
            return;
        }
        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }
        savePlayPosition(position);
        PlayActionEvent event = new PlayActionEvent(action);
        EventBus.getDefault().post(event);
    }

    /**
     * 从播放列表里点击的播放
     */
    public void playFromList(int position) {
        play(position, PlayActionEvent.ACTION_PLAY_LIST);
    }

    public void addAndPlay(List<Music> m) {
        dao.deleteAll();
        musicList.clear();
        dao.insertInTx(m);
        musicList.addAll(m);
        play(0, PlayActionEvent.ACTION_PLAY_ADD);
    }

    public void addAndPlay(Music music) {
        int position = musicList.indexOf(music);
        if (position < 0) {
            musicList.add(music);
            dao.insert(music);
            position = musicList.size() - 1;
        }
        play(position, PlayActionEvent.ACTION_PLAY_ADD);
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        if (musicList.isEmpty()) {
            toastShow("当前播放列表没有音乐");
            return;
        }
        play(getPlayPosition() + 1, PlayActionEvent.ACTION_PLAY_NEXT);
    }

    /**
     * 播放上一首
     */
    public void playPrev() {
        if (musicList.isEmpty()) {
            toastShow("当前播放列表没有音乐");
            return;
        }
        play(getPlayPosition() - 1, PlayActionEvent.ACTION_PLAY_PREV);
    }

    /**
     * 暂停
     */
    public void pause() {
        PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_PAUSE);
        EventBus.getDefault().post(event);
    }

    /**
     * 停止
     */
    public void stop() {
        PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_STOP);
        EventBus.getDefault().post(event);
    }

    /**
     * 暂停后的播放
     */
    public void playFromPause() {
        PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_PLAY_PAUSE);
        EventBus.getDefault().post(event);
    }

    /**
     * 删除歌曲
     */
    public void delete(int position) {
        int playPosition = getPlayPosition();
        Music music = musicList.remove(position);
        dao.delete(music);

        if (playPosition > position) {
            savePlayPosition(playPosition - 1);
        } else if (playPosition == position) {
            savePlayPosition(playPosition - 1);
            PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_DELETE_AND_NEXT);
            EventBus.getDefault().post(event);
        }
        PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_DELETE);
        EventBus.getDefault().post(event);
    }

}
