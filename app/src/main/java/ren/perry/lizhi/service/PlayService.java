package ren.perry.lizhi.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.event.PlayActionEvent;
import ren.perry.lizhi.helper.AudioFocusManager;
import ren.perry.lizhi.helper.AudioPlayer;
import ren.perry.lizhi.helper.MusicHelper;
import ren.perry.lizhi.receiver.MusicIntentReceiver;

/**
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class PlayService extends Service {

    private MediaPlayer player;
    private MusicIntentReceiver receiver;
    private AudioFocusManager focusManager;

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        receiver = new MusicIntentReceiver();
        focusManager = new AudioFocusManager(this);
        receiver.registerMusicReceiver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        receiver.unregisterMusicReceiver(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder implements AudioFocusManager.OnAudioFocusChangeListener, AudioFocusManager.onRequestFocusResultListener {

        private final WifiManager.WifiLock wifiLock;
        private Handler handler;
        private MediaPlayerListener mPlaybackInfoListener;

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && isPlaying()) {
                    PlayActionEvent event = new PlayActionEvent(PlayActionEvent.ACTION_PROGRESS, player.getDuration(), player.getCurrentPosition());
                    EventBus.getDefault().post(event);
                    handler.postDelayed(this, 100L);
                }
            }
        };

        MyBinder() {
            handler = new Handler(Looper.myLooper());
            mPlaybackInfoListener = new MediaPlayerListener(PlayService.this);
            wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
            wifiLock.acquire();
            focusManager.setOnAudioFocusChangeListener(this);
            focusManager.setOnHandleResultListener(this);
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
                if (player != null && music != null) {
                    player.reset();
                    player.setDataSource(music.getUrl());
                    player.prepareAsync();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void play() {
            if (player != null && !player.isPlaying()) {
                focusManager.requestFocus();
            }
        }

        public void pause() {
            if (player != null && player.isPlaying()) {
                player.pause();
                handler.removeCallbacks(runnable);
                MusicHelper.getInstance().saveCurrentDuration(player.getCurrentPosition());
                setNewState(PlaybackStateCompat.STATE_PAUSED);
            }
        }

        public boolean isPlaying() {
            return player.isPlaying();
        }

        public void close() {
            if (player != null) {
                focusManager.releaseAudioFocus();
                handler.removeCallbacks(runnable);
                setNewState(PlaybackStateCompat.STATE_STOPPED);
                MusicHelper.getInstance().saveCurrentDuration(0);
                MusicHelper.getInstance().saveTotalDuration(0);
                player.stop();
                player.reset();
                player.release();
                wifiLock.release();
                player = null;
            }
        }

        private int mState;
        private boolean mCurrentMediaPlayedToCompletion;
        private int mSeekWhileNotPlaying = -1;

        // This is the main reducer for the player state machine.
        private void setNewState(@PlaybackStateCompat.State int newPlayerState) {
            mState = newPlayerState;

            // Whether playback goes to completion, or whether it is stopped, the
            // mCurrentMediaPlayedToCompletion is set to true.
            if (mState == PlaybackStateCompat.STATE_STOPPED) {
                mCurrentMediaPlayedToCompletion = true;
            }

            // Work around for MediaPlayer.getCurrentPosition() when it changes while not playing.
            final long reportPosition;
            if (mSeekWhileNotPlaying >= 0) {
                reportPosition = mSeekWhileNotPlaying;

                if (mState == PlaybackStateCompat.STATE_PLAYING) {
                    mSeekWhileNotPlaying = -1;
                }
            } else {
                reportPosition = player == null ? 0 : player.getCurrentPosition();
            }

            final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
            stateBuilder.setActions(getAvailableActions());
            stateBuilder.setState(mState,
                    reportPosition,
                    1.0f,
                    SystemClock.elapsedRealtime());
            mPlaybackInfoListener.onPlaybackStateChange(stateBuilder.build());
        }

        @PlaybackStateCompat.Actions
        private long getAvailableActions() {
            long actions = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
            switch (mState) {
                case PlaybackStateCompat.STATE_STOPPED:
                    actions |= PlaybackStateCompat.ACTION_PLAY
                            | PlaybackStateCompat.ACTION_PAUSE;
                    break;
                case PlaybackStateCompat.STATE_PLAYING:
                    actions |= PlaybackStateCompat.ACTION_STOP
                            | PlaybackStateCompat.ACTION_PAUSE
                            | PlaybackStateCompat.ACTION_SEEK_TO;
                    break;
                case PlaybackStateCompat.STATE_PAUSED:
                    actions |= PlaybackStateCompat.ACTION_PLAY
                            | PlaybackStateCompat.ACTION_STOP;
                    break;
                default:
                    actions |= PlaybackStateCompat.ACTION_PLAY
                            | PlaybackStateCompat.ACTION_PLAY_PAUSE
                            | PlaybackStateCompat.ACTION_STOP
                            | PlaybackStateCompat.ACTION_PAUSE;
            }
            return actions;
        }

        /**
         * 焦点改变了
         */
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    //此状态表示，焦点被其他应用获取 AUDIOFOCUS_GAIN 时，触发此回调，需要暂停播放。
                    AudioPlayer.get(PlayService.this).pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂性丢失焦点，如播放视频，打电话等，需要暂停播放
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂性丢失焦点并作降音处理，看需求处理而定。
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    //别的应用申请焦点之后又释放焦点时，就会触发此回调，恢复播放音乐
                    AudioPlayer.get(PlayService.this).playFromPause();
                    break;
            }
        }

        /**
         * 获取到焦点
         * 开始播放音乐（获取焦点的事件是在播放音乐时触发的）
         */
        @Override
        public void onHandleResult(int result) {
            if (mCurrentMediaPlayedToCompletion) {
                mCurrentMediaPlayedToCompletion = false;
            }
            player.start();
            handler.post(runnable);
            MusicHelper.getInstance().saveTotalDuration(player.getDuration());
            player.setWakeMode(PlayService.this, PowerManager.PARTIAL_WAKE_LOCK);
            setNewState(PlaybackStateCompat.STATE_PLAYING);
        }
    }
}
