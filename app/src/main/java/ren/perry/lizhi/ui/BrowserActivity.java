package ren.perry.lizhi.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.R;
import ren.perry.mvplibrary.base.BaseActivity;
import ren.perry.mvplibrary.base.BasePresenter;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class BrowserActivity extends BaseActivity {

    /**
     * 链接
     */
    public static final String URL = "url";

    /**
     * 标题
     */
    public static final String TITLE = "title";

    public static void start(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        activity.startActivity(intent);
    }

    @BindView(R.id.statusBarView)
    View statusBarView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ll)
    LinearLayout ll;

    private AgentWeb mAgentWeb;

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        ImmersionBar.setStatusBarView(this, statusBarView);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    protected void initView() {
        String url = getIntent().getStringExtra(URL);
        String title = getIntent().getStringExtra(TITLE);

        tvTitle.setText(StringUtils.isEmpty(title) ? getTitle() : title);

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        };
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //do you work
            }
        };
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(webViewClient)
                .setWebChromeClient(webChromeClient)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (!mAgentWeb.back()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.ivBack})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }
}
