package ren.perry.lizhi.mvp.presenter;

import ren.perry.lizhi.bean.AlbumBean;
import ren.perry.lizhi.mvp.contract.AlbumContract;
import ren.perry.lizhi.mvp.model.AlbumModel;
import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.rx.BaseSubscriber;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public class AlbumPresenter extends AlbumContract.Presenter {

    public AlbumPresenter(AlbumContract.View view) {
        mView = view;
        mModel = new AlbumModel();
    }

    @Override
    public void album(int size, int page) {
        addSubscribe(mModel.album(size, page)
                .subscribe(new BaseSubscriber<AlbumBean>() {
                    @Override
                    public void onError(ApiException.ResponseException e) {
                        mView.onAlbumError(e);
                    }

                    @Override
                    public void onNext(AlbumBean bean) {
                        mView.onAlbumSuccess(bean);
                    }
                }));
    }
}
