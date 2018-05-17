package com.bestar.accessapp.storage.db;

import android.support.annotation.NonNull;

import com.bestar.accessapp.storage.db.module.RentHouseRecord;
import com.bestar.accessapp.storage.db.module.RentHouseRecordDao;

/**
 * Created by ted on 2018/4/23.
 * in com.bestar.accessapp.storage.db.test.source
 */
public class RentHouseDBHelper {

    private RentHouseRecordDao getDao() {
        return GreenDaoDatabase.getInstance().getDaoSession().getRentHouseRecordDao();
    }

    /***
     * 插入一条数据
     * @param rentRecord 参考{@link RentRecordBuilder }
     */
    public void insert(@NonNull RentHouseRecord rentRecord) {
        getDao().insertOrReplace(rentRecord);
    }

    public long getRecordCount() {
        return getDao().count();
    }
}
