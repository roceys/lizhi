package ren.perry.lizhi.mvp.contract;

import ren.perry.lizhi.bean.AlbumBean;
import ren.perry.mvplibrary.base.BaseModel;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.base.BaseView;
import ren.perry.mvplibrary.net.ApiException;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public interface AlbumContract {
    interface View extends BaseView {
        void onAlbumSuccess(AlbumBean bean);

        void onAlbumError(ApiException.ResponseException e);
    }

    interface Model extends BaseModel {
        Observable<AlbumBean> album(int size, int page);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void album(int size, int page);
    }
}
