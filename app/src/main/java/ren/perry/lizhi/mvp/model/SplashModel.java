package ren.perry.lizhi.mvp.model;

import ren.perry.lizhi.api.ApiEngine;
import ren.perry.lizhi.bean.SplashBean;
import ren.perry.lizhi.mvp.contract.SplashContract;
import ren.perry.mvplibrary.rx.RxSchedulers;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class SplashModel implements SplashContract.Model {
    @Override
    public Observable<SplashBean> splash() {
        return ApiEngine.getInstance()
                .getApiService()
                .splash()
                .compose(RxSchedulers.switchThread());
    }
}
