package com.bestar.accessapp.util;

import android.os.Build;

/**
 * Created by ted on 2018/4/20.
 * in com.bestar.accessapp.util
 */
public class VersionUtil {
    public static boolean isM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
