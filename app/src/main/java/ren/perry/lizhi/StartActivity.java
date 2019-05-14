package ren.perry.lizhi;

import android.content.Intent;
import android.text.TextUtils;

import ren.perry.mvplibrary.base.BaseActivity;
import ren.perry.mvplibrary.base.BasePresenter;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class StartActivity extends BaseActivity {
    @Override
    protected int initLayoutId() {
        // 判断当前activity是不是所在任务栈的根
        if (!isTaskRoot()) {
            Intent i = getIntent();
            String action = i.getAction();
            if (i.hasCategory(Intent.CATEGORY_APP_CALENDAR)
                    && !TextUtils.isEmpty(action)
                    && Intent.ACTION_MAIN.equals(action)) {
                if (!isFinishing()) finish();
            }
        }
        setTheme(R.style.AppTheme);
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        startActivity(new Intent(this, SplashActivity.class));
        if (!isFinishing()) finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isFinishing()) finish();
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }


}
