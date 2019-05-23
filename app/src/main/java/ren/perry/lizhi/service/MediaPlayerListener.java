package ren.perry.lizhi.service;

import android.content.Intent;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.helper.AudioPlayer;
import ren.perry.lizhi.notify.NotifyManager;
import ren.perry.mvplibrary.rx.RxSchedulers;

// MediaPlayerAdapter Callback: MediaPlayerAdapter state -> MusicService.
public class MediaPlayerListener extends PlaybackInfoListener {

    private final ServiceManager mServiceManager;
    private NotifyManager notifyManager;
    private PlayService mService;
    private boolean mServiceInStartedState;

    MediaPlayerListener(PlayService mService) {
        this.mService = mService;
        mServiceManager = new ServiceManager();
        notifyManager = new NotifyManager(mService);
    }

    @Override
    public void onPlaybackStateChange(PlaybackStateCompat state) {
        // Manage the started state of this service.
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                mServiceManager.moveServiceToStartedState(state);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                mServiceManager.updateNotificationForPause(state);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                mServiceManager.moveServiceOutOfStartedState(state);
                break;
        }
    }

    private class ServiceManager {

        private void moveServiceToStartedState(PlaybackStateCompat state) {
            Music music = AudioPlayer.get(mService).getPlayMusic();
            NotificationCompat.Builder builder = notifyManager.getNotificationBuilder(music, state);
            rx.Observable.just(music.getPic())
                    .map(s -> {
                        try {
                            return Glide.with(mService)
                                    .load(music.getPic())
                                    .asBitmap()
                                    .centerCrop()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .compose(RxSchedulers.switchThread())
                    .subscribe(bitmap -> {
                        if (bitmap != null) {
                            builder.setLargeIcon(bitmap);
                        }
                        if (!mServiceInStartedState) {
                            ContextCompat.startForegroundService(mService,
                                    new Intent(mService, PlayService.class));
                            mServiceInStartedState = true;
                        }
                        mService.startForeground(NotifyManager.NOTIFICATION_ID, builder.build());
                    });
        }

        private void updateNotificationForPause(PlaybackStateCompat state) {
            mService.stopForeground(false);
            Music music = AudioPlayer.get(mService).getPlayMusic();
            NotificationCompat.Builder builder = notifyManager.getNotificationBuilder(music, state);
            rx.Observable.just(music.getPic())
                    .map(s -> {
                        try {
                            return Glide.with(mService)
                                    .load(music.getPic())
                                    .asBitmap()
                                    .centerCrop()
                                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .compose(RxSchedulers.switchThread())
                    .subscribe(bitmap -> {
                        if (bitmap != null) {
                            builder.setLargeIcon(bitmap);
                        }
                        notifyManager.getNotificationManager().notify(NotifyManager.NOTIFICATION_ID, builder.build());
                    });
        }

        private void moveServiceOutOfStartedState(PlaybackStateCompat state) {
            mService.stopForeground(true);
            mService.stopSelf();
            mServiceInStartedState = false;
        }
    }

}