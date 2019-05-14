package ren.perry.mvplibrary.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * BasePresenter
 *
 * @author valentine
 * @date 2017/5/29
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected V mView;
    protected M mModel;

    protected CompositeSubscription mCompositeSubscription;

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    protected void unSubscribe() {
        if (mView != null) {
            mView = null;
        }
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }
}
