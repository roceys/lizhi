package ren.perry.mvplibrary.rx;

import android.content.Context;
import android.util.Log;

import ren.perry.mvplibrary.net.ApiException;
import ren.perry.mvplibrary.net.NetUtils;
import rx.Subscriber;

/**
 * Subscriber基类,可以在这里处理client网络连接状况
 * （比如没有wifi，没有4g，没有联网等）
 *
 * @author fcn-mq
 * @date 2017/4/19
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    @SuppressWarnings("unused")
    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @SuppressWarnings("WeakerAccess")
    public BaseSubscriber() {
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Override
    public void onStart() {
        super.onStart();
        if (context != null && !NetUtils.isConnected(context)) {
            Log.e("perry", "当前网络不可用，请检查网络情况");
            // 一定好主动调用下面这一句
            onCompleted();
            return;
        }
    }

    /**
     * onError
     *
     * @param e e
     */
    public abstract void onError(ApiException.ResponseException e);

    @Override
    public void onError(Throwable e) {
        Log.e("tag", "BaseSubscriber.throwable =" + e.toString());
        if (e instanceof Exception) {
            //访问获得对应的Exception
            onError(ApiException.handleException(e));
        } else {
            //将Throwable 和 未知错误的status code返回
            onError(new ApiException.ResponseException(e, ApiException.ErrorCode.UNKNOWN));
        }
    }

    @Override
    public void onCompleted() {
        Log.i("tag", "BaseSubscriber.onComplete()");
    }
}
