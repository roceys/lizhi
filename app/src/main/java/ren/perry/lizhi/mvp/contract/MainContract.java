package ren.perry.lizhi.mvp.contract;

import java.util.List;

import ren.perry.lizhi.bean.BannerBean;
import ren.perry.lizhi.bean.VersionBean;
import ren.perry.mvplibrary.base.BaseModel;
import ren.perry.mvplibrary.base.BasePresenter;
import ren.perry.mvplibrary.base.BaseView;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019-05-15
 * WeChat  917351143
 */
public interface MainContract {

    interface View extends BaseView {
        void onVersionSuccess(VersionBean.DataBean bean);

        void onBannerSuccess(List<BannerBean.DataBean.ListBean> bean);
    }

    interface Model extends BaseModel {
        Observable<VersionBean> version();

        Observable<BannerBean> banner();

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void version();

        public abstract void banner();
    }
}
