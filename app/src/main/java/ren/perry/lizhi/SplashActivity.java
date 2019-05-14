package ren.perry.lizhi;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.helper.SplashHelper;
import ren.perry.lizhi.mvp.contract.SplashContract;
import ren.perry.lizhi.mvp.presenter.SplashPresenter;
import ren.perry.lizhi.ui.MainActivity;
import ren.perry.lizhi.utils.UiUtils;
import ren.perry.lizhi.view.CountDownProgressView;
import ren.perry.mvplibrary.base.BaseActivity;
import ren.perry.mvplibrary.utils.GlideMan;

public class SplashActivity extends BaseActivity<SplashPresenter>
        implements CountDownProgressView.OnProgressListener, SplashContract.View {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.progressView)
    CountDownProgressView progressView;

    private SplashHelper splashHelper;

    @Override
    protected void initStatusBar() {
        //设置状态栏字体为深色
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarDarkIcon(true)
                .transparentBar()
                .init();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        splashHelper = SplashHelper.getInstance();
        String cacheUrl = splashHelper.getUrl();
        if (StringUtils.isEmpty(cacheUrl)) {
            iv.setImageResource(R.drawable.pic_splash);
        } else {
            new GlideMan.Builder()
                    .load(cacheUrl)
                    .loadingRes(R.drawable.pic_splash)
                    .loadFailRes(R.drawable.pic_splash)
                    .dotAnimation()
                    .into(iv);
        }

        //设置View的Margin，因为状态栏透明导致View顶上去了
        int r = UiUtils.dp2px(16);
        int t = r + ImmersionBar.getStatusBarHeight(this);
        UiUtils.setMargin(progressView, 0, t, r, 0);
        progressView.setProgressListener(this);
        progressView.setProgressType(CountDownProgressView.ProgressType.COUNT);
        progressView.start();
        mPresenter.splash();
    }

    @Override
    protected SplashPresenter onCreatePresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressView.stop();
        finish();
    }

    @Override
    public void onProgress(int i) {
        if (i == 100) {
            enter();
        }
    }

    /**
     * 进入APP
     */
    private void enter() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.progressView)
    public void onViewClicked() {
        progressView.stop();
        enter();
    }

    @Override
    public void onSplashSuccess(String url) {
        splashHelper.saveUrl(url);
    }
}
