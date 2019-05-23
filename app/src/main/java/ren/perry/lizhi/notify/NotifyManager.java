package ren.perry.lizhi.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.media.session.MediaButtonReceiver;

import ren.perry.lizhi.R;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.service.PlayService;
import ren.perry.lizhi.ui.MainActivity;

/**
 * @author perrywe
 * @date 2019-05-22
 * WeChat  917351143
 */
public class NotifyManager {

    private static final String CHANNEL_ID = "media_play_channel";
    private static final int REQUEST_CODE = 501;
    public static final int NOTIFICATION_ID = 412;

    private PlayService service;

    private final NotificationManager manager;
    private final NotificationCompat.Action mPlayAction;
    private final NotificationCompat.Action mPauseAction;
    private final NotificationCompat.Action mNextAction;
    private final NotificationCompat.Action mPrevAction;

    public NotifyManager(PlayService service) {
        this.service = service;
        manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        mPlayAction = new NotificationCompat.Action(R.drawable.ic_play_arrow_black_24dp, "Play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PLAY));
        mPauseAction = new NotificationCompat.Action(R.drawable.ic_pause_black_24dp, "Pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PAUSE));
        mNextAction = new NotificationCompat.Action(R.drawable.ic_skip_next_black_24dp, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_NEXT));
        mPrevAction = new NotificationCompat.Action(R.drawable.ic_skip_previous_black_24dp, "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));
        manager.cancelAll();
    }

    public NotificationManager getNotificationManager() {
        return manager;
    }

    public Notification getNotification(Music music, @NonNull PlaybackStateCompat state) {
        boolean isPlaying = state.getState() == PlaybackStateCompat.STATE_PLAYING;
        NotificationCompat.Builder builder = buildNotification(music, state, isPlaying);
        return builder.build();
    }

    public NotificationCompat.Builder getNotificationBuilder(Music music, @NonNull PlaybackStateCompat state) {
        boolean isPlaying = state.getState() == PlaybackStateCompat.STATE_PLAYING;
        return buildNotification(music, state, isPlaying);
    }

    private NotificationCompat.Builder buildNotification(Music music, @NonNull PlaybackStateCompat state, boolean isPlaying) {
        if (isAndroidOOrHigher()) {
            createChannel();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(service.getResources(), R.mipmap.default_album);
        androidx.media.app.NotificationCompat.MediaStyle mediaStyle = new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(null)
                .setShowActionsInCompactView(0, 1, 2)
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(service,
                        PlaybackStateCompat.ACTION_STOP));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, CHANNEL_ID);
        builder.setStyle(mediaStyle)
                .setColor(ContextCompat.getColor(service, R.color.app_color_accent))
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(createContentIntent())
                .setContentTitle(music.getName())
                .setContentText(music.getSinger() + " - " + music.getAlbum())
                .setLargeIcon(bitmap)
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_STOP));

        // If skip to next action is enabled.
        if ((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            builder.addAction(mPrevAction);
        }

        builder.addAction(isPlaying ? mPauseAction : mPlayAction);

        // If skip to prev action is enabled.
        if ((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            builder.addAction(mNextAction);
        }
        return builder;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            CharSequence name = "音乐播放";
            String description = "播放音乐时展示在通知栏上";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.setBypassDnd(true);
            mChannel.setShowBadge(false);
            mChannel.enableLights(false);
            mChannel.enableVibration(false);
            manager.createNotificationChannel(mChannel);
        }
    }

    private boolean isAndroidOOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 创建不会重复启动的Intent
     */
    private PendingIntent createContentIntent() {
        Intent openUI = new Intent(service, MainActivity.class);
        openUI.setAction(Intent.ACTION_MAIN);
        openUI.addCategory(Intent.CATEGORY_LAUNCHER);
        openUI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return PendingIntent.getActivity(service, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
