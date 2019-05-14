package ren.perry.mvplibrary.net;

import android.webkit.WebSettings;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import ren.perry.mvplibrary.BaseMvpApplication;
import ren.perry.mvplibrary.BuildConfig;
import ren.perry.mvplibrary.PerryMvp;

/**
 * ren.perry.mvpLibrary.net
 *
 * @author perry
 * @date 2017/10/19
 * Wechat  917351143
 */

public class NetClient {
    private volatile static NetClient netClient;
    private final OkHttpClient client;

    private NetClient() {

        //缓存
        int size = 1024 * 1024 * 100;
        File cacheFile = new File(BaseMvpApplication.getContext().getCacheDir(), "OkHttpCache");
        Cache cache = new Cache(cacheFile, size);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置连接超时
        builder.connectTimeout(PerryMvp.getInstance().getConnectTimeout(), TimeUnit.SECONDS);
        //设置读超时
        builder.readTimeout(PerryMvp.getInstance().getReadTimeout(), TimeUnit.SECONDS);
        //设置写超时
        builder.writeTimeout(PerryMvp.getInstance().getWriteTimeout(), TimeUnit.SECONDS);

        builder.addNetworkInterceptor(new NetWorkInterceptor());
        if (BuildConfig.DEBUG) {
            //日志拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        //添加默认Header（在初始化BaseMvpApplication时传过来的）
        addHeaders(builder, PerryMvp.getInstance().getHeaders());
        builder.cache(cache);
        client = builder.build();
    }

    /**
     * 添加Headers
     *
     * @param builder OkHttpClient.Builder
     */
    private void addHeaders(OkHttpClient.Builder builder, List<Map<String, String>> headers) {
        Interceptor headerInterceptor = chain -> {
            Request.Builder builder1 = chain.request().newBuilder();
            builder1.removeHeader("User-Agent");
            builder1.addHeader("User-Agent", WebSettings.getDefaultUserAgent(PerryMvp.getInstance().getContext()));
            if (headers != null && headers.size() > 0) {
                for (Map<String, String> map : headers) {
                    if (map != null) {
                        Set<String> set = map.keySet();
                        for (String key : set) {
                            builder1.addHeader(key, Objects.requireNonNull(map.get(key)));
                        }
                    }
                }
            }
            return chain.proceed(builder1.build());
        };
        builder.addInterceptor(headerInterceptor);
    }

    public static NetClient getInstance() {
        if (netClient == null) {
            synchronized (NetClient.class) {
                if (netClient == null) {
                    netClient = new NetClient();
                }
            }
        }
        return netClient;
    }

    public OkHttpClient getOkHttpClient() {
        return client;
    }
}
