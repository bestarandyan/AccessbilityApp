package com.bestar.accessapp.storage.db.test.source;

import android.support.annotation.NonNull;

import com.bestar.accessapp.storage.db.GreenDaoDatabase;
import com.bestar.accessapp.storage.db.test.ActionRecord;
import com.bestar.accessapp.storage.db.GreenDaoDatabase;
import com.bestar.accessapp.storage.db.test.ActionRecord;
import com.bestar.accessapp.storage.db.test.ActionRecordDao;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by ted on 2018/4/20.
 * in com.bestar.accessapp.storage.db.test.source
 */
public class ActionRecordLocalSource implements ActionRecordSource {

    public ActionRecordLocalSource() {
    }

    private ActionRecordDao getDao(){
        return GreenDaoDatabase.getInstance().getDaoSession().getActionRecordDao();
    }

    @Override
    public Flowable<List<ActionRecord>> getActionRecords() {
        return Flowable.fromCallable(new Callable<List<ActionRecord>>() {
            @Override
            public List<ActionRecord> call() {
                List<ActionRecord> list = getDao().loadAll();
                //Logger.d("getActionRecords size == " + list.size());
                return list;
            }
        });
    }

    @Override
    public Observable<ActionRecord> getActionRecordById(final long id) {
        return Observable.fromCallable(new Callable<ActionRecord>() {
            @Override
            public ActionRecord call() {
                return getDao().load(id);
            }
        });
    }

    @Override
    public void saveActionRecord(@NonNull ActionRecord actionRecord) {
        getDao().insertOrReplace(actionRecord);
    }

    @Override
    public void deleteActionRecord(long id) {
        getDao().deleteByKey(id);
    }

    @Override
    public long getActionRecordCount() {
        return getDao().count();
    }
}
