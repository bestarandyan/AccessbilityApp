package com.bestar.accessapp.util;

/**
 * Created by Ted on 2015/12/29.
 *
 * 点击类工具类
 */
public class Click {
  private static long lastClickTime;

  /***
   * 是否快速双击
   * @return boolen
   */
  public static boolean isDblclick() {
    long time = System.currentTimeMillis();
    if (time - lastClickTime < 500) {
      return true;
    }
    lastClickTime = time;
    return false;
  }
}
