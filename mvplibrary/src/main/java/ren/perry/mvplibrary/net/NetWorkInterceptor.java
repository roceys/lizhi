package ren.perry.mvplibrary.net;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ren.perry.mvplibrary.BaseMvpApplication;

/**
 * 网络拦截器
 *
 * @author Nicholas
 * @date 2016/11/6
 */

class NetWorkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //无网络时强制使用缓存
        if (!NetUtils.isConnected(BaseMvpApplication.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        /*
         * 有网络时，设置超时为0
         * 无网络时，设置超时为1周
         * removeHeader: 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
         */
        if (NetUtils.isConnected(BaseMvpApplication.getContext())) {
            int maxStale = 0;
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 7;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
