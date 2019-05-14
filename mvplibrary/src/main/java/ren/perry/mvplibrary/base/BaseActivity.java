package ren.perry.mvplibrary.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import ren.perry.mvplibrary.R;
import ren.perry.mvplibrary.utils.AppManager;

/**
 * BaseActivity
 *
 * @author perry
 * @date 2017/5/29
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;
    protected Context context;
    protected Bundle savedInstanceState;

    /**
     * 初始化布局
     *
     * @return layout
     */
    protected abstract int initLayoutId();

    /**
     * 执行逻辑代码
     */
    protected abstract void initView();

    /**
     * 设置状态栏
     */
    protected void initStatusBar() {
        //沉浸式状态栏 默认为亮色
        ImmersionBar.with(this)
//                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.white)
                .navigationBarDarkIcon(true)
                .init();
    }

    /**
     * 创建Presenter
     *
     * @return P extend BasePresenter
     */
    protected abstract P onCreatePresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;

        initLayoutId();
        super.onCreate(savedInstanceState);
        if (onCreatePresenter() != null) {
            mPresenter = onCreatePresenter();
        }
        setContentView(initLayoutId());
        AppManager.addActivity(this);
        ButterKnife.bind(this);
        context = this;
        initStatusBar();
        initView();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
        AppManager.finishActivity(this);
    }

    protected void toastShow(String msg) {
        Toasty.normal(this, msg).show();
    }
}
