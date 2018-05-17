package com.bestar.accessapp.storage.db.test.source;

import android.support.annotation.NonNull;

import com.bestar.accessapp.storage.db.test.ActionRecord;
import com.bestar.accessapp.storage.db.test.ActionRecord;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by ted on 2018/4/20.
 * in com.bestar.accessapp.storage.db.test.source
 */
public interface ActionRecordSource {
    Flowable<List<ActionRecord>> getActionRecords();

    Observable<ActionRecord> getActionRecordById(long rowId);

    void saveActionRecord(@NonNull ActionRecord actionRecord);

    void deleteActionRecord(long id);

    long getActionRecordCount();
}
