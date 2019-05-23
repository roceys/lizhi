package ren.perry.lizhi.mvp.model;

import ren.perry.lizhi.api.ApiEngine;
import ren.perry.lizhi.bean.AboutBean;
import ren.perry.lizhi.mvp.contract.AboutContract;
import ren.perry.mvplibrary.rx.RxSchedulers;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-23
 * WeChat  917351143
 */
public class AboutModel implements AboutContract.Model {
    @Override
    public Observable<AboutBean> about() {
        return ApiEngine.getInstance()
                .getApiService()
                .about()
                .compose(RxSchedulers.switchThread());
    }
}
