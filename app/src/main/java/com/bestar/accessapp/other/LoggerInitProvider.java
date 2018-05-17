package com.bestar.accessapp.other;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import timber.log.Timber;

/**
 * Created by ted on 2018/4/20.
 * in com.bestar.accessapp.other
 */
public class LoggerInitProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        Timber.plant(new Timber.DebugTree());
        Timber.tag("accessapp");
        return true;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }
}
