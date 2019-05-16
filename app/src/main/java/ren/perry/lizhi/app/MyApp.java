package ren.perry.lizhi.app;

import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.greenrobot.greendao.query.QueryBuilder;

import ren.perry.lizhi.dao.DaoManager;
import ren.perry.mvplibrary.BaseMvpApplication;

/**
 * @author perrywe
 * @date 2019-05-14
 * WeChat  917351143
 */
public class MyApp extends BaseMvpApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initNetworkTimeout();
        initLogger();
        initGreenDao();
    }

    /**
     * 初始化SQLite数据库
     */
    private void initGreenDao() {
        DaoManager.get().init(this);
        if (Constants.App.isDebug) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    /**
     * 初始化网络请求超时时间
     */
    private void initNetworkTimeout() {
        setTimeout(20, 15, 15);
    }

    /**
     * 初始化日志打印器
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  //不显示线程信息
                .methodCount(0)         //方法数
                .methodOffset(7)        //内部方法偏移量
                .tag("Perry_thin")      //自定义全局Tag
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                //只在Debug模式下打印日志
                return Constants.App.isDebug;
            }
        });
    }
}
