package ren.perry.lizhi.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import org.apache.commons.lang3.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

import ren.perry.lizhi.helper.AudioPlayer;

public class MusicIntentReceiver extends BroadcastReceiver {

    private int clickCount;
    private Timer timer = new Timer();

    private Context context;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        this.context = ctx;
        String action = intent.getAction();
        if (!StringUtils.isEmpty(action) && !"".equals(action)) {
            switch (action) {
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    //耳机插拔事件
                    AudioPlayer.get(ctx).pause();
                    break;
                case Intent.ACTION_MEDIA_BUTTON:
                    //媒体按钮事件
                    KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                    int keyCode = keyEvent.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                            AudioPlayer.get(ctx).playFromPause();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            AudioPlayer.get(ctx).pause();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            AudioPlayer.get(ctx).playNext();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            AudioPlayer.get(ctx).playPrev();
                            break;
                    }

                    //处理耳机按钮控制
                    if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        clickCount = clickCount + 1;
                        if (clickCount == 1) {
                            HeadsetTimerTask headsetTimerTask = new HeadsetTimerTask();
                            timer.schedule(headsetTimerTask, 1000);
                        }
                    } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT) {
                        handler.sendEmptyMessage(2);
                    } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                        handler.sendEmptyMessage(3);
                    }
                    break;
            }
        }
    }

    class HeadsetTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                if (clickCount == 1) {
                    handler.sendEmptyMessage(1);
                } else if (clickCount == 2) {
                    handler.sendEmptyMessage(2);
                } else if (clickCount >= 3) {
                    handler.sendEmptyMessage(3);
                }
                clickCount = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    AudioPlayer.get(context).playOrPause();
                } else if (msg.what == 2) {
                    AudioPlayer.get(context).playNext();
                } else if (msg.what == 3) {
                    AudioPlayer.get(context).playPrev();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void registerMusicReceiver(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ComponentName name = new ComponentName(context.getPackageName(), MusicIntentReceiver.class.getName());
        audioManager.registerMediaButtonEventReceiver(name);
    }

    public void unregisterMusicReceiver(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ComponentName name = new ComponentName(context.getPackageName(), MusicIntentReceiver.class.getName());
        audioManager.unregisterMediaButtonEventReceiver(name);
    }
}