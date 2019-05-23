package ren.perry.lizhi.ui;

import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import ren.perry.lizhi.R;
import ren.perry.lizhi.app.Constants;
import ren.perry.lizhi.bean.AboutBean;
import ren.perry.lizhi.helper.ConfigHelper;
import ren.perry.lizhi.mvp.contract.AboutContract;
import ren.perry.lizhi.mvp.presenter.AboutPresenter;
import ren.perry.mvplibrary.base.BaseFragment;
import ren.perry.mvplibrary.net.ApiException;

/**
 * @author perrywe
 * @date 2019-05-23
 * WeChat  917351143
 */
public class AboutFragment extends BaseFragment<AboutPresenter> implements AboutContract.View {

    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    static Fragment getInstance() {
        return new AboutFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected AboutPresenter onCreatePresenter() {
        return new AboutPresenter(this);
    }

    @Override
    protected void initView() {
        String versionStr = "当前版本：" + Constants.App.versionName;
        tvVersion.setText(versionStr);

        //初始化刷新控件
        refreshLayout.setColorSchemeResources(R.color.app_color, R.color.app_color_dark, R.color.app_color_accent);
        refreshLayout.setOnRefreshListener(this::fetchData);
        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
    }

    @Override
    protected void fetchData() {
        mPresenter.about();
    }

    @Override
    public void onAboutSuccess(AboutBean bean) {
        refreshLayout.setRefreshing(false);
        if (bean.getCode() == 1) {
            if (bean.getData().getType() == 1) {
                String content = bean.getData().getContent();
                ConfigHelper.getInstance().saveAbout(content);
                tvInfo.setText(bean.getData().getContent());
            }
        }
    }

    @Override
    public void onAboutError(ApiException.ResponseException e) {
        refreshLayout.setRefreshing(false);
        String content = ConfigHelper.getInstance().getAbout();
        tvInfo.setText(content);
    }
}
