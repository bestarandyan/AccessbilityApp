package com.bestar.accessapp.util;

import java.io.DataOutputStream;
import java.io.IOException;

import timber.log.Timber;

/**
 * User:chenzhe
 * Date: 2018/4/24
 * Time:16:12
 */
public class RootUtil {

    private static final String TAG = "RootCmd";
    private static boolean mHaveRoot = false;
    /**
     *   判断机器Android是否已经root，即是否获取root权限
     */
    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
            if (ret != -1) {
                Timber.i("have root!");
                mHaveRoot = true;
            } else {
                Timber.i("not root!");
            }
        } else {
            Timber.i("mHaveRoot = true, have root!");
        }
        return mHaveRoot;
    }



    /**
     * 执行命令但不关注结果输出
     */
    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            if(p == null){
                return result;
            }
            dos = new DataOutputStream(p.getOutputStream());
            Timber.i("执行adb命令" + cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
