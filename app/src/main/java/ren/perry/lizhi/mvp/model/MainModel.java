package ren.perry.lizhi.mvp.model;

import ren.perry.lizhi.api.ApiEngine;
import ren.perry.lizhi.app.Constants;
import ren.perry.lizhi.bean.BannerBean;
import ren.perry.lizhi.bean.VersionBean;
import ren.perry.lizhi.mvp.contract.MainContract;
import ren.perry.mvplibrary.rx.RxSchedulers;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class MainModel implements MainContract.Model {
    @Override
    public Observable<VersionBean> version() {
        String appId = Constants.App.appId;
        return ApiEngine.getInstance()
                .getApiService()
                .version(appId)
                .compose(RxSchedulers.switchThread());
    }

    @Override
    public Observable<BannerBean> banner() {
        return ApiEngine.getInstance()
                .getApiService()
                .banner()
                .compose(RxSchedulers.switchThread());
    }
}
