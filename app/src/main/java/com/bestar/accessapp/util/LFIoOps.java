package com.bestar.accessapp.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import timber.log.Timber;

/**
 * Created by ted on 2018/4/28.
 * in com.bestar.accessapp.util
 */
public class LFIoOps {

    private static final String SD_ROOT_FOLDER_NAME = "wkzf";

    /**
     * 得到当前存储设备的目录
     */
    public static final String SD_ROOT_PATH = Environment.getExternalStorageDirectory()
            + File.separator + SD_ROOT_FOLDER_NAME + File.separator;
    /**
     * 获取扩展SD卡设备状态
     */
    private static String SDStateString = Environment.getExternalStorageState();

    public static void initBaseAppFolder(Context context) {

        if (isHaveStorage()) {
            File baseFolder = new File(Environment.getExternalStorageDirectory()
                    + File.separator + SD_ROOT_FOLDER_NAME);
            if (!baseFolder.isDirectory() || !baseFolder.exists()) {
                boolean result = baseFolder.mkdir();
                Timber.d("创建文件结果" + result);
            }
        } else {
            Timber.e("初始化文件夹失败");
        }
    }

    private static boolean isHaveStorage() {
        if (!SDStateString.equals(Environment.MEDIA_MOUNTED)) {
            Timber.e("SD card is not available/write able right now.");
            return false;
        }
        return true;
    }
}
