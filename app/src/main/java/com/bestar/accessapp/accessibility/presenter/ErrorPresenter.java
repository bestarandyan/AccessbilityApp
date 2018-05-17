package com.bestar.accessapp.accessibility.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.notification.WarningUtil;

import java.util.List;

import timber.log.Timber;

/**
 * User:chenzhe
 * Date: 2018/4/19
 * Time:17:37
 */
public class ErrorPresenter extends BasePresenter {
    private static ErrorPresenter mErrorPresenter;
    private TAccessibilityInterface mInterface;

    int errorCount = 0;

    public static ErrorPresenter getIntance(TAccessibilityInterface tInterface) {
        if (mErrorPresenter == null) {
            synchronized (ListDataPresenter.class) {
                if (mErrorPresenter == null) {
                    mErrorPresenter = new ErrorPresenter();
                }
            }
        }
        mErrorPresenter.setTAccessibilityInterface(tInterface);
        return mErrorPresenter;
    }

    private void setTAccessibilityInterface(TAccessibilityInterface tInterface) {
        mInterface = tInterface;
    }

    /***
     * 处理无障碍消息事件
     *
     */
    public void onHandleAccessibilityEvent(int eventType) {
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Timber.i("ev    entType :AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Timber.i("ev    entType :AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
                dealError();
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void dealError() {
        if (mInterface == null) {
            return;
        }
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
//com.wuba:id/title_left_btn  列表页 的返回
//com.wuba:id/detail_top_bar_left_big_btn  列表页 的返回
        if(rootNodeInfo == null){
            return;
        }
        final List<AccessibilityNodeInfo> errorNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/RequestError");
        final List<AccessibilityNodeInfo> returnNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/detail_top_bar_left_big_btn");
        final List<AccessibilityNodeInfo> warningNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/warning_close");
        final List<AccessibilityNodeInfo> errorNodes2 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/activity_rn_common_error_view");
        final List<AccessibilityNodeInfo> errorNodes3 = rootNodeInfo.findAccessibilityNodeInfosByViewId("加载更多失败");
        final List<AccessibilityNodeInfo> errorNodes4 = rootNodeInfo.findAccessibilityNodeInfosByText("服务器异常，请稍后再试喔~");


        if (errorNodes.size() > 0 && returnNodes.size() > 0) {
            addCount(errorNodes, returnNodes); //网络错误1
        } else if (warningNodes.size() > 0) {//广告页面
            warningNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else if (errorNodes2.size() > 0) {//不可点击的网络异常    //网络错误2
//            ((TAccessibilityService) mInterface).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);//直接返回
            //报警
            WarningUtil.getInstance().errorStoped(BaseApp.getContext());
        } else if (errorNodes3.size() > 0) {//加载更多失败按钮出现  //网络错误3
            clickMore(errorNodes3);
        } else if (errorNodes4.size() > 0) {//网络错误4
            String idResourceName = errorNodes4.get(0).getViewIdResourceName();
            if (!TextUtils.isEmpty(idResourceName) && idResourceName.equals("com.wuba:id/RequestLoadingErrorText")) {
                //报警
                WarningUtil.getInstance().errorStoped(BaseApp.getContext());
//                ((TAccessibilityService) mInterface).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);//直接返回
            }
        }
    }


    private void clickMore(List<AccessibilityNodeInfo> errorNodes3) {
        //报警
        WarningUtil.getInstance().errorStoped(BaseApp.getContext());
        errorNodes3.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    private void addCount(List<AccessibilityNodeInfo> errorNodes, List<AccessibilityNodeInfo> returnNodes) {
        synchronized (this) {
            errorCount++;
            if (errorCount > 5) {
                //处理错误
                errorCount = 0;
                //报警
                WarningUtil.getInstance().errorStoped(BaseApp.getContext());
//                returnNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                ((TAccessibilityService) mInterface).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            } else {
                errorNodes.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean isMyEvent() {
        if (mInterface == null) {
            return false;
        }
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return false;
        }
        final List<AccessibilityNodeInfo> errorNodes1 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/RequestLoadingButton");
        final List<AccessibilityNodeInfo> errorNodes2 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/loadingError_image");
        final List<AccessibilityNodeInfo> errorNodes3 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/warning_close"); //广告页面
        final List<AccessibilityNodeInfo> errorNodes4 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/activity_rn_common_error_view");//无网络页面4
        final List<AccessibilityNodeInfo> errorNodes5 = rootNodeInfo.findAccessibilityNodeInfosByText("加载更多失败");//加载更多失败
        final List<AccessibilityNodeInfo> errorNodes6 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/RequestLoadingErrorText");///无网络页面4
        final List<AccessibilityNodeInfo> errorNodes7 = rootNodeInfo.findAccessibilityNodeInfosByText("服务器异常，请稍后再试喔~");///无网络页面4
        final List<AccessibilityNodeInfo> errorNodes8 = rootNodeInfo.findAccessibilityNodeInfosByText("内容已失效，看看别的吧");///内容失效，配合errorNodes2 处理

        if ((errorNodes1.size() != 0 && errorNodes2.size() != 0) || errorNodes3.size() != 0 || errorNodes4.size() != 0 || errorNodes5.size() > 0
                || (errorNodes6.size() > 0 && errorNodes7.size() > 0) || (errorNodes2.size() > 0 && errorNodes8.size() > 0)) {
            if (errorNodes2.size() > 0 && errorNodes8.size() > 0) {
                Log.e("andy", "处理了-----内容已失效");
            }
            return true;
        }
        return false;
    }

}
