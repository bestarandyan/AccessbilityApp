package com.bestar.accessapp;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * User:chenzhe
 * Date: 2018/4/19
 * Time:11:55
 */
public class BaseApp extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
        Stetho.initializeWithDefaults(this);
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    private void initDB() {

    }

}
