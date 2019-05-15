package ren.perry.lizhi.mvp.model;

import ren.perry.lizhi.api.ApiEngine;
import ren.perry.lizhi.bean.AlbumBean;
import ren.perry.lizhi.mvp.contract.AlbumContract;
import ren.perry.mvplibrary.rx.RxSchedulers;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class AlbumModel implements AlbumContract.Model {
    @Override
    public Observable<AlbumBean> album(int size, int page) {
        return ApiEngine.getInstance()
                .getApiService()
                .album(size, page)
                .compose(RxSchedulers.switchThread());
    }
}
