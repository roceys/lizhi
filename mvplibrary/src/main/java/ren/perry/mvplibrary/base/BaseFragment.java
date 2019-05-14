package ren.perry.mvplibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * BaseFragment
 *
 * @author perry
 * @date 2017/5/29
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    private Unbinder unbinder;

    protected P mPresenter;

    /**
     * 宿主Activity
     */
    protected FragmentActivity activity;

    /**
     * 根View
     */
    protected View rootView;

    protected boolean mIsViewInitiated;
    protected boolean mIsVisibleToUser;
    protected boolean mIsDataInitiated;

    /**
     * 仅第一次可见时调用
     */
    protected void fetchData() {
    }

    protected Bundle savedInstanceState;

    /**
     * 设置跟布局的资源ID
     *
     * @return layout
     */
    protected abstract int initLayoutId();

    /**
     * 创建Presenter
     *
     * @return P extends BasePresenter
     */
    protected abstract P onCreatePresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsViewInitiated = true;
        initFetchData();
    }

    protected void initFetchData() {
        if (mIsVisibleToUser && mIsViewInitiated && !mIsDataInitiated) {
            fetchData();
            mIsDataInitiated = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        initFetchData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(initLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (onCreatePresenter() != null) {
            mPresenter = onCreatePresenter();
        }
        this.savedInstanceState = savedInstanceState;
        initData(getArguments());
        initView();
        return rootView;
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     *
     * @param arguments 接受到的从其他地方传过来的参数
     */
    @SuppressWarnings("UnnecessaryReturnStatement")
    protected void initData(Bundle arguments) {
        if (arguments == null) {
            return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
        unbinder.unbind();
    }

    protected void toastShow(String msg) {
        Toasty.normal(activity, msg).show();
    }
}
