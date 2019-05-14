package ren.perry.mvplibrary.rx;


import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * author : Perry
 * time   : 2017/6/12
 *
 * @author perry
 */

@SuppressWarnings("unused")
public class FuncTry {
    /**
     * 出错的重试
     *
     * @return Func1
     */
    public static Func1<Observable<? extends Throwable>, Observable<?>> tryError() {
        return observable -> observable.zipWith(
                Observable.range(7000, 5),
                (Func2<Throwable, Integer, Integer>) (throwable, attempt) -> attempt);
    }
}
