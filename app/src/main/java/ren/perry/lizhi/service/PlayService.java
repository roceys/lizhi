package ren.perry.lizhi.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.event.PlayActionEvent;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class PlayService extends Service {

    private MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        private final WifiManager.WifiLock wifiLock;
        private Music music;

        private Handler handler;

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_PROGRESS, player.getDuration(), player.getCurrentPosition());
                    EventBus.getDefault().post(event);
                    handler.postDelayed(this, 100L);
                }
            }
        };

        public MyBinder() {
            handler = new Handler(Looper.myLooper());
            wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
            wifiLock.acquire();
        }

        public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
            player.setOnPreparedListener(listener);
        }

        public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
            player.setOnCompletionListener(listener);
        }

        public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
            player.setOnErrorListener(listener);
        }

        public MediaPlayer getPlayer() {
            return player;
        }

        public void setDataSource(Music music) {
            try {
                this.music = music;
                player.reset();
                player.setDataSource(music.getUrl());
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void play() {
            if (!player.isPlaying()) {
                player.start();
                handler.post(runnable);
                player.setWakeMode(PlayService.this, PowerManager.PARTIAL_WAKE_LOCK);
            }
        }

        public void pause() {
            if (player.isPlaying()) {
                player.pause();
                handler.removeCallbacks(runnable);
            }
        }

        public boolean isPlaying() {
            return player.isPlaying();
        }

        public void close() {
            if (player != null) {
                player.stop();
                player.reset();
                player.release();
                wifiLock.release();
            }
        }

        public int getProgress() {
            return player.getDuration();
        }

        public int getPlayPosition() {
            return player.getCurrentPosition();
        }

        public void seekToPosition(int s) {
            player.seekTo(s);
        }

        public Music getPlayMusic() {
            return music;
        }
    }
}
