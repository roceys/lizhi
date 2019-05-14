package ren.perry.mvplibrary.utils;

import android.content.Context;

/**
 * SharedPreferences的工具类
 * <p>
 *
 * @author Wpl
 * @date 2017/1/3
 * Contact number 18244267955 and Email pl.w@outlook.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SpUtils {
    //加密的SharedPreferences
    private SecurePreferences preferences;
    private SecurePreferences.Editor editor;

    public SpUtils(Context context, String fileName) {
        preferences = new SecurePreferences(context, "wei199532", fileName);
        editor = preferences.edit();
    }

    /**
     * SP写入key对应的数据
     *
     * @param key   键
     * @param value 值
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 清空SP的所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key 键
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取SP指定key的值
     *
     * @param key      键
     * @param defValue 默认值
     * @return 值
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }
}
