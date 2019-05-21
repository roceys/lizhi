package ren.perry.lizhi.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.R;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.event.PlayActionEvent;
import ren.perry.lizhi.helper.AudioPlayer;
import ren.perry.lizhi.utils.MathUtils;
import ren.perry.lizhi.view.CircularMusicProgressBar;
import ren.perry.mvplibrary.base.BaseActivity;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.utils.GlideMan;

/**
 * @author perrywe
 * @date 2019-05-21
 * WeChat  917351143
 */
public class PlayerActivity extends BaseActivity {
    @BindView(R.id.statusBarView)
    View statusBarView;
    @BindView(R.id.pb)
    CircularMusicProgressBar pb;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.ibPlay)
    ImageButton ibPlay;

    @Subscribe
    public void onPlayEvent(PlayActionEvent event) {
        switch (event.getAction()) {
            case PlayActionEvent.ACTION_PLAY_ADD:           //播放新添加的
                loadUI(true);
                break;
            case PlayActionEvent.ACTION_PLAY_LIST:          //播放从播放列表里点击的
                loadUI(true);
                break;
            case PlayActionEvent.ACTION_PLAY_PAUSE:         //暂停后的播放
                loadUI(true);
                break;
            case PlayActionEvent.ACTION_PLAY_NEXT:          //下一曲
                loadUI(true);
                break;
            case PlayActionEvent.ACTION_PLAY_PREV:          //上一曲
                loadUI(true);
                break;
            case PlayActionEvent.ACTION_PAUSE:              //暂停
                loadUI(false);
                break;
            case PlayActionEvent.ACTION_STOP:               //停止
                break;
            case PlayActionEvent.ACTION_DELETE:             //删除
                break;
            case PlayActionEvent.ACTION_DELETE_AND_NEXT:    //删除了正在播放的
                break;
            case PlayActionEvent.ACTION_PROGRESS:           //播放进度
                int total = event.getTotalProgress();
                int current = event.getCurrentProgress();
                float value = MathUtils.getPercentage(current, total);
                pb.setValueWithNoAnimation(value);
                break;
        }
    }

    private void loadUI(boolean isPlaying) {
        Music music = AudioPlayer.get(this).getPlayMusic();
        ibPlay.setSelected(isPlaying);
        if (music != null) {
            tvName.setText(music.getName());
            String infoStr = music.getSinger() + " - " + music.getAlbum();
            tvInfo.setText(infoStr);
            new GlideMan.Builder()
                    .load(music.getPic())
                    .circle()
                    .dotAnimation()
                    .loadingRes(R.drawable.default_cover)
                    .loadFailRes(R.drawable.default_cover)
                    .into(pb);
        } else {
            tvName.setText("无音乐");
            tvInfo.setText("当前没有要播放的音乐");
            pb.setImageResource(R.drawable.default_cover);
        }
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        ImmersionBar.setStatusBarView(this, statusBarView);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        boolean isPlaying = getIntent().getBooleanExtra("isPlaying", false);
        loadUI(isPlaying);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }


    @OnClick({R.id.ivBack, R.id.ibLoop, R.id.ibPrev, R.id.ibPlay, R.id.ibNext, R.id.ibList})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ibLoop:
                toastShow("列表循环");
                break;
            case R.id.ibPrev:
                AudioPlayer.get(this).playPrev();
                break;
            case R.id.ibPlay:
                doPlay();
                break;
            case R.id.ibNext:
                AudioPlayer.get(this).playNext();
                break;
            case R.id.ibList:
                startActivity(new Intent(this, PlayListActivity.class));
                break;
        }
    }

    private void doPlay() {
        if (ibPlay.isSelected()) {
            AudioPlayer.get(this).pause();
        } else {
            if (AudioPlayer.get(this).getPlayMusic() != null) {
                AudioPlayer.get(this).playFromPause();
            } else {
                toastShow("当前播放列表没有音乐");
            }
        }
    }
}
