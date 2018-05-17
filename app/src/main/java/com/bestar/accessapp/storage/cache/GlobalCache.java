package com.bestar.accessapp.storage.cache;

import com.bestar.accessapp.model.RecordItem;
import com.bestar.accessapp.model.RecordItem;

import java.util.ArrayList;

/**
 * Created by ted on 2018/4/17.
 * in com.tedxiong.helper.data
 */
public class GlobalCache {
    private static ArrayList<RecordItem> allRecordList = new ArrayList<>();

    public static void addRecord(RecordItem record) {
        if (null == record || !record.isValid() || hasSame(record)) return;
        allRecordList.add(record);
    }

    public static ArrayList<RecordItem> getAllRecordList() {
        return allRecordList;
    }

    private static boolean hasSame(RecordItem recordItem) {
        boolean result = false;
        for (RecordItem item : allRecordList) {
            if (item.sameAs(recordItem)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
