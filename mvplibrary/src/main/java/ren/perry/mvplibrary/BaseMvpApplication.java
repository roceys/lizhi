package ren.perry.mvplibrary;

import android.app.Application;

import java.util.List;
import java.util.Map;

/**
 * ren.perry.mvpLibrary
 *
 * @author perry
 * @date 2017/10/19
 * WeChat  917351143
 */

public abstract class BaseMvpApplication extends Application {
    private static BaseMvpApplication mvpApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mvpApplication == null) {
            mvpApplication = this;
        }
        PerryMvp.getInstance().setContext(mvpApplication);
    }

    /**
     * 设置超时时间
     *
     * @param connect 连接超时
     * @param read    读超时
     * @param write   写超时
     */
    public void setTimeout(int connect, int read, int write) {
        PerryMvp.getInstance().setConnectTimeout(connect);
        PerryMvp.getInstance().setReadTimeout(read);
        PerryMvp.getInstance().setWriteTimeout(write);
    }

    /**
     * 添加默认Headers
     *
     * @param headers headers列表
     */
    public void setDefaultNetworkHeaders(List<Map<String, String>> headers) {
        PerryMvp.getInstance().setHeaders(headers);
    }

    public static BaseMvpApplication getContext() {
        return mvpApplication;
    }

}
