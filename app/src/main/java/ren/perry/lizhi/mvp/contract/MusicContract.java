package ren.perry.lizhi.mvp.contract;

import ren.perry.lizhi.bean.MusicBean;
import ren.perry.mvplibrary.base.BaseModel;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.base.BaseView;
import ren.perry.mvplibrary.net.ApiException;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
public interface MusicContract {

    interface View extends BaseView {
        void onMusicSuccess(MusicBean bean);

        void onMusicError(ApiException.ResponseException e);
    }

    interface Model extends BaseModel {
        Observable<MusicBean> music(int albumId, int size, int page);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void music(int albumId, int size, int page);
    }
}
