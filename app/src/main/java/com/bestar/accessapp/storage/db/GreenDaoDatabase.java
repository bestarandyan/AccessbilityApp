package com.bestar.accessapp.storage.db;

import android.content.Context;
import android.support.annotation.NonNull;


import com.bestar.accessapp.storage.db.test.DaoMaster;
import com.bestar.accessapp.storage.db.test.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ted on 2018/4/23.
 * in com.bestar.accessapp.storage.db
 */
public class GreenDaoDatabase {
    private static DaoSession mDaoSession;
    private static final boolean ENCRYPTED = false;

    private GreenDaoDatabase() {
    }

    private static class SingletonHolder {
        private static final GreenDaoDatabase INSTANCE = new GreenDaoDatabase();
    }

    public static GreenDaoDatabase getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        init(context, "wkzf_accessapp");
    }

    public void init(@NonNull Context context, @NonNull String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), dbName);
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            throw new NullPointerException("green db has not been initialized");
        }
        return mDaoSession;
    }
}
