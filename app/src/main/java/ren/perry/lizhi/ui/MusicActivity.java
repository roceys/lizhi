package ren.perry.lizhi.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.R;
import ren.perry.lizhi.adapter.MusicRvAdapter;
import ren.perry.lizhi.bean.MusicBean;
import ren.perry.lizhi.mvp.contract.MusicContract;
import ren.perry.lizhi.mvp.presenter.MusicPresenter;
import ren.perry.lizhi.utils.UiUtils;
import ren.perry.mvplibrary.base.BaseActivity;
import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.utils.GlideMan;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class MusicActivity extends BaseActivity<MusicPresenter>
        implements MusicContract.View, BaseQuickAdapter.OnItemChildClickListener {

    public static final String KEY_ALBUM_NAME = "KEY_ALBUM_NAME";   //专辑名称
    public static final String KEY_ALBUM_ID = "KEY_ALBUM_ID";       //专辑ID
    public static final String KEY_ALBUM_IMG = "KEY_ALBUM_IMG";     //专辑图片

    private int mId;

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.fabTop)
    FloatingActionButton fabTop;

    private int pageCount = 1;
    private boolean isLoadMore = false;
    private int lastPosition;
    private final int visibleFabPosition = 6;
    private int requestCount = 20;

    private MusicRvAdapter rvAdapter;

    public static void start(Activity activity, String name, int id, String img) {
        Intent intent = new Intent(activity, MusicActivity.class);
        intent.putExtra(KEY_ALBUM_NAME, name);
        intent.putExtra(KEY_ALBUM_ID, id);
        intent.putExtra(KEY_ALBUM_IMG, img);
        activity.startActivity(intent);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_music;
    }

    @Override
    protected void initView() {
        mId = getIntent().getIntExtra(KEY_ALBUM_ID, 0);
        String mName = getIntent().getStringExtra(KEY_ALBUM_NAME);
        String mImg = getIntent().getStringExtra(KEY_ALBUM_IMG);

//        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
//        lp.width = UiUtils.getScreenWidth();
//        lp.height = UiUtils.getScreenWidth();
//        imageView.setLayoutParams(lp);

        new GlideMan.Builder().load(mImg).into(imageView);

        UiUtils.setMargin(toolbar, 0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setTitle(mName);
        collapsingToolbarLayout.setTitle(mName);
        //设置收缩前字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(UiUtils.getColor(R.color.white));
        //设置收缩后字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(UiUtils.getColor(R.color.white));

        //初始化刷新控件
        refreshLayout.setColorSchemeResources(R.color.app_color, R.color.app_color_dark, R.color.app_color_accent);
        refreshLayout.setOnRefreshListener(() -> {
            pageCount = 1;
            fetchData();
        });

        //初始化RecyclerView
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        rvAdapter = new MusicRvAdapter(mName);
        rvAdapter.bindToRecyclerView(recyclerView);
        rvAdapter.setOnItemChildClickListener(this);
        rvAdapter.setEnableLoadMore(true);
        rvAdapter.setOnLoadMoreListener(() -> {
            pageCount++;
            isLoadMore = true;
            fetchData();
        }, recyclerView);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = lm.findLastVisibleItemPosition();
                if (lastPosition < position && lastPosition >= visibleFabPosition) {
                    fabTop.show();
                }
                if (lastPosition > position && fabTop.isShown()) {
                    fabTop.hide();
                }
                lastPosition = position;
            }
        });
        refreshLayout.post(() -> {
            refreshLayout.setRefreshing(true);
            fetchData();
        });
    }

    private void fetchData() {
        mPresenter.music(mId, requestCount, pageCount);
    }

    @Override
    protected MusicPresenter onCreatePresenter() {
        return new MusicPresenter(this);
    }

    @Override
    public void onMusicSuccess(MusicBean bean) {
        refreshLayout.setRefreshing(false);
        if (bean.getCode() == 1) {
            if (isLoadMore) {
                isLoadMore = false;
                if (bean.getData().getList().size() >= 1) {
                    rvAdapter.addData(bean.getData().getList());
                }
                if (bean.getData().getList().size() == requestCount) {
                    rvAdapter.loadMoreComplete();
                }
                if (bean.getData().getList().size() < requestCount) {
                    rvAdapter.loadMoreEnd();
                }
            } else {
                String countStr = "共" + bean.getData().getCount() + "首";
                tvCount.setText(countStr);
                if (bean.getData().getList().size() >= 1) {
                    rvAdapter.setNewData(bean.getData().getList());
                    if (bean.getData().getList().size() < requestCount) {
                        rvAdapter.loadMoreEnd();
                    }
                } else {
                    rvAdapter.setNewData(null);
                    rvAdapter.setEmptyView(UiUtils.getRvMsgView("啊噢~没有获取到数据诶"));
                }
            }
        } else {
            if (isLoadMore) {
                isLoadMore = false;
                pageCount -= 1;
                rvAdapter.loadMoreFail();
            } else {
                rvAdapter.setNewData(null);
                rvAdapter.setEmptyView(UiUtils.getRvMsgView(bean.getMsg()));
            }
        }

    }

    @Override
    public void onMusicError(ApiException.ResponseException e) {
        refreshLayout.setRefreshing(false);
        if (isLoadMore) {
            isLoadMore = false;
            pageCount -= 1;
            rvAdapter.loadMoreFail();
        } else {
            rvAdapter.setNewData(null);
            rvAdapter.setEmptyView(UiUtils.getRvMsgView(e.message));
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @OnClick({R.id.llPlayAll, R.id.fabTop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llPlayAll:
                break;
            case R.id.fabTop:
                recyclerView.smoothScrollToPosition(0);
                break;
        }
    }
}
