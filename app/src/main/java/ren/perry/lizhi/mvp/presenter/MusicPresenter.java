package ren.perry.lizhi.mvp.presenter;

import ren.perry.lizhi.bean.MusicBean;
import ren.perry.lizhi.mvp.contract.MusicContract;
import ren.perry.lizhi.mvp.model.MusicModel;
import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.rx.BaseSubscriber;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public class MusicPresenter extends MusicContract.Presenter {

    public MusicPresenter(MusicContract.View view) {
        mView = view;
        mModel = new MusicModel();
    }

    @Override
    public void music(int albumId, int size, int page) {
        addSubscribe(mModel.music(albumId, size, page)
                .subscribe(new BaseSubscriber<MusicBean>() {
                    @Override
                    public void onError(ApiException.ResponseException e) {
                        mView.onMusicError(e);
                    }

                    @Override
                    public void onNext(MusicBean bean) {
                        mView.onMusicSuccess(bean);
                    }
                }));
    }
}
