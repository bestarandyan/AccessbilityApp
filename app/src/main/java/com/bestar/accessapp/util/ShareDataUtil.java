package com.bestar.accessapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.BaseApp;

/**
 * User:chenzhe
 * Date: 2018/4/26
 * Time:11:31
 */
public class ShareDataUtil {

    static Context mContext = BaseApp.getContext();
    static String mName = "accessapp_data";
    /**
     * 将int数据放入Shared
     * @param key     索引值
     * @param value   存入数据
     */
    public static synchronized void putString(
                                           String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(mName, 0);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 从Shared获取int数据
     *
     * @param key     索引值
     * @return 结果（默认值为0）
     */
    @Deprecated
    public static String getString(String key) {
        return getString(key, "");
    }


    /**
     * 从Shared获取int数据
     *
     * @param key      索引值
     * @param defValue 默认值
     * @return 结果
     */
    public static String getString(String key,
                             String defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(mName, 0);
        return sp.getString(key, defValue);
    }


}
