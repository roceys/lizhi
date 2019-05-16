package ren.perry.lizhi.mvp.model;

import ren.perry.lizhi.api.ApiEngine;
import ren.perry.lizhi.bean.MusicBean;
import ren.perry.lizhi.mvp.contract.MusicContract;
import ren.perry.mvplibrary.rx.RxSchedulers;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class MusicModel implements MusicContract.Model {
    @Override
    public Observable<MusicBean> music(int albumId, int size, int page) {
        return ApiEngine.getInstance()
                .getApiService()
                .music(albumId, size, page)
                .compose(RxSchedulers.switchThread());
    }
}
