package com.bestar.accessapp.util;

import java.util.Date;

/**
 * Created by ted on 2018/4/23.
 * in com.bestar.accessapp.util
 */
public class DateUtil {

    public static long getDayTime() {
        long now = System.currentTimeMillis();
        long dayMillis = 24 * 60 * 60 * 1000;
        return now / dayMillis;
    }

    public static Date getToday(){
        return new Date();
    }
}
