package ren.perry.lizhi.ui;

import android.graphics.Color;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ren.perry.lizhi.R;
import ren.perry.lizhi.adapter.GlideImageLoader;
import ren.perry.lizhi.adapter.PageAdapter;
import ren.perry.lizhi.app.Constants;
import ren.perry.lizhi.bean.BannerBean;
import ren.perry.lizhi.bean.VersionBean;
import ren.perry.lizhi.mvp.contract.MainContract;
import ren.perry.lizhi.mvp.presenter.MainPresenter;
import ren.perry.lizhi.utils.UiUtils;
import ren.perry.mvplibrary.base.BaseActivity;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

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

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

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

        mPresenter.banner();
        mPresenter.version();
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
}
