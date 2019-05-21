package ren.perry.lizhi.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ren.perry.lizhi.R;
import ren.perry.lizhi.adapter.PlayRvAdapter;
import ren.perry.lizhi.entity.Music;
import ren.perry.lizhi.event.PlayActionEvent;
import ren.perry.lizhi.helper.AudioPlayer;
import ren.perry.lizhi.mvp.contract.PlayContract;
import ren.perry.lizhi.mvp.presenter.PlayPresenter;
import ren.perry.lizhi.utils.UiUtils;
import ren.perry.mvplibrary.base.BaseActivity;

/**
 * 播放列表
 *
 * @author perrywe
 * @date 2019-05-17
 * WeChat  917351143
 */
public class PlayListActivity extends BaseActivity<PlayPresenter>
        implements BaseQuickAdapter.OnItemChildClickListener, PlayContract.View {
    @BindView(R.id.statusBarView)
    View statusBarView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
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
    private int requestCount = 20;

    private PlayRvAdapter rvAdapter;

    @Subscribe
    public void onPlayEvent(PlayActionEvent event) {
        switch (event.getAction()) {
            case PlayActionEvent.ACTION_PLAY_ADD:           //播放新添加的
                rvAdapter.notifyDataSetChanged();
                break;
            case PlayActionEvent.ACTION_PLAY_LIST:          //播放从播放列表里点击的
                rvAdapter.notifyDataSetChanged();
                break;
            case PlayActionEvent.ACTION_PLAY_PAUSE:         //暂停后的播放
                break;
            case PlayActionEvent.ACTION_PLAY_NEXT:          //下一曲
                rvAdapter.notifyDataSetChanged();
                break;
            case PlayActionEvent.ACTION_PLAY_PREV:          //上一曲
                rvAdapter.notifyDataSetChanged();
                break;
            case PlayActionEvent.ACTION_PAUSE:              //暂停
                break;
            case PlayActionEvent.ACTION_STOP:               //停止
                break;
            case PlayActionEvent.ACTION_DELETE:             //删除
                rvAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_play_list;
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        ImmersionBar.setStatusBarView(this, statusBarView);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        tvTitle.setText(getTitle());

        //初始化刷新控件
        refreshLayout.setColorSchemeResources(R.color.app_color, R.color.app_color_dark, R.color.app_color_accent);
        refreshLayout.setOnRefreshListener(() -> {
            pageCount = 1;
            fetchData();
        });

        //初始化RecyclerView
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.VERTICAL);
        rvAdapter = new PlayRvAdapter(this);
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
        mPresenter.getMusicList(requestCount, pageCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected PlayPresenter onCreatePresenter() {
        return new PlayPresenter(this);
    }

    @OnClick({R.id.ivBack, R.id.fabTop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.fabTop:
                recyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onMusicSuccess(List<Music> music) {
        refreshLayout.setRefreshing(false);
        if (isLoadMore) {
            isLoadMore = false;
            if (music.size() >= 1) {
                rvAdapter.addData(music);
            }
            if (music.size() == requestCount) {
                rvAdapter.loadMoreComplete();
            }
            if (music.size() < requestCount) {
                rvAdapter.loadMoreEnd();
            }
        } else {
            if (music.size() >= 1) {
                rvAdapter.setNewData(music);
                if (music.size() < requestCount) {
                    rvAdapter.loadMoreEnd();
                }
            } else {
                rvAdapter.setNewData(null);
                rvAdapter.setEmptyView(UiUtils.getRvMsgView("啊噢~没有获取到数据诶"));
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Music music = (Music) adapter.getData().get(position);
        switch (view.getId()) {
            case R.id.rlItem:
                AudioPlayer.get(this).playFromList(position);
                break;
            case R.id.ibMore:
                new MaterialDialog.Builder(this)
                        .items("移除此歌曲")
                        .itemsCallback((dialog, itemView, position1, text) -> {
                            if (position1 == 0) {
                                AudioPlayer.get(this).delete(position);
                                rvAdapter.remove(position);
                                rvAdapter.notifyDataSetChanged();
                            }
                        }).show();
                break;
        }
    }
}
