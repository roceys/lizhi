package ren.perry.lizhi.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.R;
import ren.perry.lizhi.adapter.AlbumRvAdapter;
import ren.perry.lizhi.bean.AlbumBean;
import ren.perry.lizhi.mvp.contract.AlbumContract;
import ren.perry.lizhi.mvp.presenter.AlbumPresenter;
import ren.perry.lizhi.utils.UiUtils;
import ren.perry.mvplibrary.base.BaseFragment;
import ren.perry.mvplibrary.net.ApiException;

/**
 * 歌单
 *
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class AlbumFragment extends BaseFragment<AlbumPresenter> implements AlbumContract.View, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fabTop)
    FloatingActionButton fabTop;

    private int pageCount = 1;
    private boolean isLoadMore = false;
    private int lastPosition;
    private final int visibleFabPosition = 6;

    private AlbumRvAdapter rvAdapter;

    private int requestCount = 21;

    public static Fragment getInstance() {
        return new AlbumFragment();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    protected AlbumPresenter onCreatePresenter() {
        return new AlbumPresenter(this);
    }

    @Override
    protected void initView() {

        //初始化刷新控件
        refreshLayout.setColorSchemeResources(R.color.app_color, R.color.app_color_dark, R.color.app_color_accent);
        refreshLayout.setOnRefreshListener(() -> {
            pageCount = 1;
            fetchData();
        });

        //初始化RecyclerView
        GridLayoutManager lm = new GridLayoutManager(activity, 3);
        lm.setOrientation(RecyclerView.VERTICAL);
        rvAdapter = new AlbumRvAdapter(lm);
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

        refreshLayout.post(() -> refreshLayout.setRefreshing(true));
    }

    @Override
    protected void fetchData() {
        mPresenter.album(requestCount, pageCount);
    }

    @Override
    public void onAlbumSuccess(AlbumBean bean) {
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
                if (bean.getData().getList().size() >= 1) {
                    rvAdapter.setNewData(bean.getData().getList());
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
    public void onAlbumError(ApiException.ResponseException e) {
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

    @OnClick(R.id.fabTop)
    public void onViewClicked() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        AlbumBean.DataBean.ListBean bean = (AlbumBean.DataBean.ListBean) adapter.getData().get(position);
        if (view.getId() == R.id.cv) {
            toastShow(bean.getName());
        }

    }
}
