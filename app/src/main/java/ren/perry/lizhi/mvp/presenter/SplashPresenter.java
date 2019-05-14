package ren.perry.lizhi.mvp.presenter;

import ren.perry.lizhi.bean.SplashBean;
import ren.perry.lizhi.mvp.contract.SplashContract;
import ren.perry.lizhi.mvp.model.SplashModel;
import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.rx.BaseSubscriber;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class SplashPresenter extends SplashContract.Presenter {

    public SplashPresenter(SplashContract.View view) {
        mView = view;
        mModel = new SplashModel();
    }

    @Override
    public void splash() {
        addSubscribe(mModel.splash()
                .subscribe(new BaseSubscriber<SplashBean>() {
                    @Override
                    public void onError(ApiException.ResponseException e) {

                    }

                    @Override
                    public void onNext(SplashBean bean) {
                        if (bean.getCode() == 1) {
                            mView.onSplashSuccess(bean.getData().getUrl());
                        }
                    }
                }));

    }
}
