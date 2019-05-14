package ren.perry.mvplibrary.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Rx线程调度
 *
 * @author valentine
 * @date 2017/5/29
 */

public class RxSchedulers {
    public static <T> Observable.Transformer<T, T> switchThread() {
        return tObservable -> tObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
