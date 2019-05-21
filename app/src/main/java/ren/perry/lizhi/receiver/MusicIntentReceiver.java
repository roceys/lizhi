package ren.perry.lizhi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import org.apache.commons.lang3.StringUtils;

import ren.perry.lizhi.helper.AudioPlayer;

public class MusicIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        String action = intent.getAction();
        if (!StringUtils.isEmpty(action) && !"".equals(action)) {
            switch (action) {
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    AudioPlayer.get(ctx).pause();
                    break;
            }
        }
    }
}