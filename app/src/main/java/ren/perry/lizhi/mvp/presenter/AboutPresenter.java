package ren.perry.lizhi.mvp.presenter;

import ren.perry.lizhi.bean.AboutBean;
import ren.perry.lizhi.mvp.contract.AboutContract;
import ren.perry.lizhi.mvp.model.AboutModel;
import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.rx.BaseSubscriber;

/**
 * @author perrywe
 * @date 2019-05-23
 * WeChat  917351143
 */
public class AboutPresenter extends AboutContract.Presenter {

    public AboutPresenter(AboutContract.View view) {
        mView = view;
        mModel = new AboutModel();
    }

    @Override
    public void about() {
        addSubscribe(mModel.about()
                .subscribe(new BaseSubscriber<AboutBean>() {
                    @Override
                    public void onError(ApiException.ResponseException e) {
                        mView.onAboutError(e);
                    }

                    @Override
                    public void onNext(AboutBean bean) {
                        mView.onAboutSuccess(bean);
                    }
                }));
    }
}
