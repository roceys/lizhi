package ren.perry.lizhi.mvp.contract;

import ren.perry.lizhi.bean.AboutBean;
import ren.perry.mvplibrary.base.BaseModel;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.base.BaseView;
import ren.perry.mvplibrary.net.ApiException;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-23
 * WeChat  917351143
 */
public interface AboutContract {

    interface View extends BaseView {
        void onAboutSuccess(AboutBean bean);

        void onAboutError(ApiException.ResponseException e);
    }

    interface Model extends BaseModel {
        Observable<AboutBean> about();
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void about();
    }
}
