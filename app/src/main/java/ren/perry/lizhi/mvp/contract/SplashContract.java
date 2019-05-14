package ren.perry.lizhi.mvp.contract;

import ren.perry.lizhi.bean.SplashBean;
import ren.perry.mvplibrary.base.BaseModel;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.base.BaseView;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public interface SplashContract {
    interface View extends BaseView {
        void onSplashSuccess(String url);
    }

    interface Model extends BaseModel {
        Observable<SplashBean> splash();

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void splash();
    }
}
