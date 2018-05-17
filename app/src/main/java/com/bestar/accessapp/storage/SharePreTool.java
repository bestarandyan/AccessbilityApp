package com.bestar.accessapp.storage;

import com.cocosw.favor.AllFavor;
import com.cocosw.favor.Default;

/**
 * Created by ted on 2018/4/20.
 * in com.bestar.accessapp.storage
 */
@AllFavor
public interface SharePreTool {
    @Default("false")
    boolean getAppEnable();///主功能是否打开

    @Default("true")
    boolean getJobServiceEnable();//监听服务是否打开

    @Default("false")
    boolean isCheckDrawOverlays();//打开窗口显示的权限提示

    @Default("false")
    boolean isCheckAccessibility();//是否主动检查了无障碍权限设置

    @Default("-1")
    int getFloatViewX();//上次圆形浮窗的位置x

    @Default("-1")
    int getFloatViewY();//上次圆形浮窗的位置y


    /*************************/
    void setCheckDrawOverlays(boolean status);

    void setCheckAccessibility(boolean status);

    void setAppEnable(boolean status);

    void setJobServiceEnable(boolean status);

    void setFloatViewX(int floatViewX);

    void setFloatViewY(int floatViewY);
}
