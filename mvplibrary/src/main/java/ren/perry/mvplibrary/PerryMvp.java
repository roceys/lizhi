package ren.perry.mvplibrary;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * ren.perry.mvpLibrary
 *
 * @author perry
 * @date 2017/10/19
 * WeChat  917351143
 */

public class PerryMvp {
    private volatile static PerryMvp perryMvp;

    private PerryMvp() {
    }

    /**
     * 获取单例对象
     *
     * @return PerryMvp
     */
    public static PerryMvp getInstance() {
        if (perryMvp == null) {
            synchronized (PerryMvp.class) {
                if (perryMvp == null) {
                    perryMvp = new PerryMvp();
                }
            }
        }
        return perryMvp;
    }

    /**
     * 连接超时
     */
    private int connectTimeout;
    /**
     * 读超时
     */
    private int readTimeout;
    /**
     * 写超时
     */
    private int writeTimeout;

    /**
     * 添加默认Headers
     */
    private List<Map<String, String>> headers;

    private Context context;

    void setContext(Context context) {
        this.context = context;
    }

    void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    void setHeaders(List<Map<String, String>> headers) {
        this.headers = headers;
    }

    public Context getContext() {
        return context;
    }

    public int getConnectTimeout() {
        return connectTimeout == 0 ? 15 : connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout == 0 ? 12 : readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout == 0 ? 12 : writeTimeout;
    }

    public List<Map<String, String>> getHeaders() {
        return headers;
    }


}
