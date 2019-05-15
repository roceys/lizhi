package ren.perry.lizhi.mvp.presenter;

import ren.perry.lizhi.bean.BannerBean;
import ren.perry.lizhi.bean.VersionBean;
import ren.perry.lizhi.mvp.contract.MainContract;
import ren.perry.lizhi.mvp.model.MainModel;
import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.rx.BaseSubscriber;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class MainPresenter extends MainContract.Presenter {

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = new MainModel();
    }

    @Override
    public void version() {
        addSubscribe(mModel.version()
                .subscribe(new BaseSubscriber<VersionBean>() {
                    @Override
                    public void onError(ApiException.ResponseException e) {

                    }

                    @Override
                    public void onNext(VersionBean bean) {
                        if (bean.getCode() == 1 && bean.getData() != null) {
                            mView.onVersionSuccess(bean.getData());
                        }
                    }
                }));
    }

    @Override
    public void banner() {
        addSubscribe(mModel.banner()
                .subscribe(new BaseSubscriber<BannerBean>() {
                    @Override
                    public void onError(ApiException.ResponseException e) {
                    }

                    @Override
                    public void onNext(BannerBean bean) {
                        if (bean.getCode() == 1
                                && bean.getData() != null
                                && bean.getData().getList().size() > 0) {
                            mView.onBannerSuccess(bean.getData().getList());
                        }
                    }
                }));
    }
}
