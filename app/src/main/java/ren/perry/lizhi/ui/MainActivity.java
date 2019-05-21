package ren.perry.lizhi.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.R;
import ren.perry.lizhi.adapter.GlideImageLoader;
import ren.perry.lizhi.adapter.PageAdapter;
import ren.perry.lizhi.app.Constants;
import ren.perry.lizhi.bean.BannerBean;
import ren.perry.lizhi.bean.VersionBean;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.event.PlayActionEvent;
import ren.perry.lizhi.helper.AudioPlayer;
import ren.perry.lizhi.helper.MusicHelper;
import ren.perry.lizhi.mvp.contract.MainContract;
import ren.perry.lizhi.mvp.presenter.MainPresenter;
import ren.perry.lizhi.service.PlayService;
import ren.perry.lizhi.utils.UiUtils;
import ren.perry.mvplibrary.base.BaseActivity;
import ren.perry.mvplibrary.utils.GlideMan;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class MainActivity extends BaseActivity<MainPresenter>
        implements MainContract.View, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bannerView)
    Banner bannerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar1)
    Toolbar toolbar1;
    @BindView(R.id.ivBar)
    ImageView ivBar;
    @BindView(R.id.tvBarTop)
    TextView tvBarTop;
    @BindView(R.id.tvBarBottom)
    TextView tvBarBottom;
    @BindView(R.id.ibBarPlay)
    ImageButton ibBarPlay;
    @BindView(R.id.pbBar)
    ProgressBar pbBar;
    @BindView(R.id.llBar)
    RelativeLayout llBar;

    private ServiceConnection serviceConnection;
    private PlayService.MyBinder myBinder;

    @Subscribe
    public void onPlayEvent(PlayActionEvent event) {
        switch (event.getAction()) {
            case PlayActionEvent.ACTION_PLAY_ADD:           //播放新添加的
                play();
                break;
            case PlayActionEvent.ACTION_PLAY_LIST:          //播放从播放列表里点击的
                play();
                break;
            case PlayActionEvent.ACTION_PLAY_PAUSE:         //暂停后的播放
                myBinder.play();
                ibBarPlay.setSelected(true);
                break;
            case PlayActionEvent.ACTION_PLAY_NEXT:          //下一曲
                play();
                break;
            case PlayActionEvent.ACTION_PLAY_PREV:          //上一曲
                play();
                break;
            case PlayActionEvent.ACTION_PAUSE:              //暂停
                myBinder.pause();
                ibBarPlay.setSelected(false);
                break;
            case PlayActionEvent.ACTION_STOP:               //停止
                break;
            case PlayActionEvent.ACTION_DELETE:             //删除
                break;
            case PlayActionEvent.ACTION_DELETE_AND_NEXT:    //删除了正在播放的
                //这里是防止position<0时被重置成了0（删除第一天，position会变成-1的）
                int position = MusicHelper.getInstance().getPosition();
                //保存position为下一曲
                MusicHelper.getInstance().savePosition(position + 1);
                if (myBinder.isPlaying()) {
                    //播放下一曲
                    PlayActionEvent e = new PlayActionEvent(PlayActionEvent.ACTION_PLAY_NEXT);
                    EventBus.getDefault().post(e);
                } else {
                    myBinder.getPlayer().reset();
                    loadBar(AudioPlayer.get(this).getPlayMusic(), false);
                }
                break;
            case PlayActionEvent.ACTION_PROGRESS:           //播放进度
                int total = event.getTotalProgress();
                int current = event.getCurrentProgress();
                pbBar.setMax(total);
                pbBar.setProgress(current);
                break;
        }
    }

    private void loadBar(Music music, boolean isPlaying) {
        ibBarPlay.setSelected(isPlaying);
        if (music != null) {
            tvBarTop.setText(music.getName());
            String infoStr = music.getSinger() + " - " + music.getAlbum();
            tvBarBottom.setText(infoStr);
            new GlideMan.Builder()
                    .load(music.getPic())
                    .circle()
                    .loadingRes(R.drawable.default_cover)
                    .loadFailRes(R.drawable.default_cover)
                    .into(ivBar);
        } else {
            tvBarTop.setText("无音乐");
            tvBarBottom.setText("当前没有要播放的音乐");
            ivBar.setImageResource(R.drawable.default_cover);
        }
    }

    private void play() {
        Music music = AudioPlayer.get(this).getPlayMusic();
        myBinder.setDataSource(music);
        myBinder.setOnPreparedListener(mp -> {
            myBinder.setOnCompletionListener(MainActivity.this);
            myBinder.setOnErrorListener(MainActivity.this);
            myBinder.play();
        });
        loadBar(music, true);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        appBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            int scrollRange = appBarLayout.getTotalScrollRange();
            int alpha = (int) Math.abs(255f / scrollRange * i);
            //设置状态栏背景为灰色
            toolbar1.setBackgroundColor(Color.argb(alpha, 136, 136, 136));
        });

        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
        params.height = ImmersionBar.getStatusBarHeight(this);
        toolbar.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = toolbar1.getLayoutParams();
        params1.height = ImmersionBar.getStatusBarHeight(this);
        toolbar1.setLayoutParams(params1);

        initFragment();
        initTabLayout();
        initBannerView();
        initPlayerService();

        mPresenter.banner();
        mPresenter.version();
    }

    private void initPlayerService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (PlayService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent playServiceIntent = new Intent(this, PlayService.class);
        bindService(playServiceIntent, serviceConnection, BIND_AUTO_CREATE);

        loadBar(AudioPlayer.get(this).getPlayMusic(), false);
    }

    private void initFragment() {
        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();

        //=================================================
        titles.add("歌单");
        fragments.add(AlbumFragment.getInstance());
        //-----------------
//        titles.add("关于");
//        fragments.add();
        //=================================================

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.setTitles(titles);
        pageAdapter.setFragments(fragments);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(titles.size() - 1);
    }

    private void initTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(UiUtils.getColor(R.color.normal), UiUtils.getAppColor());
        tabLayout.setSelectedTabIndicatorColor(UiUtils.getAppColor());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initBannerView() {
        bannerView.setImageLoader(new GlideImageLoader());
        bannerView.setBannerAnimation(Transformer.Accordion);
        bannerView.setDelayTime(5000);
        bannerView.setIndicatorGravity(BannerConfig.CENTER);
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onVersionSuccess(VersionBean.DataBean bean) {
        if (Constants.App.versionCode < bean.getVersion_code()) {
            UIData uiData = UIData.create()
                    .setTitle(bean.getTitle())
                    .setContent(bean.getContent())
                    .setDownloadUrl(bean.getUrl());
            DownloadBuilder db = AllenVersionChecker.getInstance()
                    .downloadOnly(uiData);
            if (bean.getIs_force() == 1) {
                db.setForceUpdateListener(() -> {
                });
            }
            db.executeMission(this);
        }
    }

    @Override
    public void onBannerSuccess(List<BannerBean.DataBean.ListBean> bean) {
        List<String> titles = new ArrayList<>();
        List<String> images = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (BannerBean.DataBean.ListBean b : bean) {
            titles.add(b.getTitle());
            images.add(b.getImg());
            urls.add(b.getUrl());
        }
        bannerView.setImages(images);
        bannerView.setBannerTitles(titles);
        bannerView.setOnBannerListener(position -> {
            //轮播图点击事件
            BrowserActivity.start(this, urls.get(position), titles.get(position));
        });
        bannerView.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bannerView != null) {
            bannerView.stopAutoPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(serviceConnection);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.ibBarPlay, R.id.ibBarNext, R.id.ibBarList, R.id.llBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBarPlay:
                if (ibBarPlay.isSelected()) {
                    ibBarPlay.setSelected(false);
                    myBinder.pause();
                } else {
                    if (AudioPlayer.get(this).getPlayMusic() != null) {
                        AudioPlayer.get(this).playFromPause();
                    } else {
                        toastShow("当前播放列表没有音乐");
                    }
                }
                break;
            case R.id.ibBarNext:
                AudioPlayer.get(this).playNext();
                break;
            case R.id.ibBarList:
                startActivity(new Intent(this, PlayListActivity.class));
                break;
            case R.id.llBar:
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("isPlaying", myBinder.isPlaying());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        ibBarPlay.setSelected(false);
        AudioPlayer.get(this).playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        toastShow("播放失败");
        new Handler().postDelayed(() -> AudioPlayer.get(this).playNext(), 1000);
        return false;
    }
}
