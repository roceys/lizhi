package ren.perry.lizhi.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * @author perrywe
 * @date 2019/4/17
 * WeChat  917351143
 */
public class MyDexOpenHelper extends DaoMaster.DevOpenHelper {
    public MyDexOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyDexOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        // TODO: 2019/4/17 新增的Dao层都添加在下面
        MigrationHelper.getInstance().migrate(db, MusicDao.class);
    }
}
