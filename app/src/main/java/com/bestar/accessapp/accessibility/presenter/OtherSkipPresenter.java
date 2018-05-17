package com.bestar.accessapp.accessibility.presenter;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.util.RootUtil;

import java.util.List;

import timber.log.Timber;

/**
 * User:chenzhe
 * Date: 2018/4/20
 * Time:15:20
 */
public class OtherSkipPresenter extends BasePresenter {
    private static final String TAG = OtherSkipPresenter.class.getSimpleName();
    private static OtherSkipPresenter mOtherSkipPresenter;
    private TAccessibilityInterface mInterface;
    private PagerType pagerType = null;

    public static OtherSkipPresenter getInstance(TAccessibilityInterface tInterface) {
        if (mOtherSkipPresenter == null) {
            synchronized (OtherSkipPresenter.class) {
                if (mOtherSkipPresenter == null) {
                    mOtherSkipPresenter = new OtherSkipPresenter();
                }
            }
        }
        mOtherSkipPresenter.setTAccessibilityInterface(tInterface);
        return mOtherSkipPresenter;
    }

    private void setTAccessibilityInterface(TAccessibilityInterface tInterface) {
        mInterface = tInterface;
    }
   /* public ListDataPresenter(TAccessibilityInterface tInterface) {
        mInterface = tInterface;
    }*/


    /***
     * 处理无障碍消息事件
     *
     */
    public void onHandleAccessibilityEvent(int eventType) {
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Timber.i("eventType :AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Timber.i("eventType :AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
                clickButton();
                break;
        }

    }

    private void clickButton() {
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if(rootNodeInfo == null){
            return;
        }
        final List<AccessibilityNodeInfo> titlesNodes1 = rootNodeInfo.findAccessibilityNodeInfosByText("租房");
        if (pagerType == PagerType.TYPE_INDEX && titlesNodes1.size() > 0) {
            AccessibilityNodeInfo nodes = findNodes(titlesNodes1);
            if (nodes != null) { //找到控件
                nodes.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else if (pagerType == PagerType.TYPE_SECOND) {
            findNodes2(rootNodeInfo);

            // 可以不用在 Activity 中增加任何处理，各 Activity 都可以响应

           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    if (wholeRentNodeInfo != null) {
                        Instrumentation inst = new Instrumentation();
                        Rect rect = new Rect();
                        wholeRentNodeInfo.getBoundsInScreen(rect);
                        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_DOWN, rect.left + 20, rect.top + 20, 0));
                        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_UP, rect.left + 20, rect.top + 20, 0));
                        wholeRentNodeInfo = null;
                    }
                }
            }).start();*/
            if (wholeRentNodeInfo != null) {
                int childCount = wholeRentNodeInfo.getParent().getChildCount();
                Timber.i("子孩子" + childCount);
//                wholeRentNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if(wholeRentNodeInfo.getParent() != null){
                    executeAdb(wholeRentNodeInfo.getParent());
                }
                wholeRentNodeInfo = null;
            }
        }else if(pagerType == PagerType.SORT_PAGER){
            clickSort(rootNodeInfo);
        }
    }

    private void executeAdb(AccessibilityNodeInfo wholeRentNodeInfo) {
       if(RootUtil.haveRoot()){
           Rect rect = new Rect();
           wholeRentNodeInfo.getBoundsInScreen(rect);
           String cmd = "input tap " + (rect.left + 20) + " " + (rect.top + 20);
           RootUtil.execRootCmdSilent(cmd);
       }

    }

    private void clickSort(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> sortNodes2 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/tradeline_filter_list_item_content");//筛选的节点
        if(sortNodes2.size() > 0){
            for (final AccessibilityNodeInfo nodeInfo : sortNodes2) {
                if(!TextUtils.isEmpty(nodeInfo.getText()) && "最近更新".equals(nodeInfo.getText().toString())){
                    new Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if(nodeInfo.getParent() != null){
                                        nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        ListDataPresenter.changeSortStatus(true);
                                    }
                                }
                            }
                    ,2000);

                    break;
                }
            }

        }
    }

    /**
     * 找首页面的node
     *
     * @param titlesNodes1
     * @return
     */
    public AccessibilityNodeInfo findNodes(List<AccessibilityNodeInfo> titlesNodes1) {
        AccessibilityNodeInfo correctNodeInfo = null;
        for (int i = 0; i < titlesNodes1.size(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = titlesNodes1.get(i);
            String idResourceName = accessibilityNodeInfo.getViewIdResourceName();
            if (!TextUtils.isEmpty(idResourceName) && "com.wuba:id/textView".equals(idResourceName)) {
                correctNodeInfo = accessibilityNodeInfo;
                break;
            }
        }
        return correctNodeInfo;
    };
    AccessibilityNodeInfo wholeRentNodeInfo = null;

    /**
     * 找第二个页面的node
     *
     * @param rootNodeInfo
     * @return
     */
    public void findNodes2(AccessibilityNodeInfo rootNodeInfo) {

        for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = rootNodeInfo.getChild(i);
            int childCount = child.getChildCount();
            if (childCount > 0) {
                findNodes2(child);
            } else {
                if (child.getContentDescription() != null) {
                    String text = child.getContentDescription().toString();
                    if (!TextUtils.isEmpty(text) && text.equals("个人房源")) {
                        wholeRentNodeInfo = child;
                        return;
                    }
                }
            }

        }

    }

    @Override
    public boolean isMyEvent() {
        if (mInterface == null) {
            return false;
        }
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return false;
        }
        final List<AccessibilityNodeInfo> searchNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/search_bar");//有搜索栏
        final List<AccessibilityNodeInfo> tabNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/new_tabs");//有Tab
        final List<AccessibilityNodeInfo> hometabNodes1 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/home_tab_icon");//首页的节点
        final List<AccessibilityNodeInfo> hometabNodes2 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/tab_layout");//首页的节点

        final List<AccessibilityNodeInfo> sortNodes1 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/sort_list_view");//筛选的节点
        final List<AccessibilityNodeInfo> sortNodes2 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/tradeline_filter_list_item_content");//筛选的节点


        if ((searchNodes.size() != 0 && tabNodes.size() != 0)) {
            pagerType = PagerType.TYPE_SECOND;
            ListDataPresenter.changeSortStatus(false);
            return true;
        }
        if ((hometabNodes1.size() > 0 && hometabNodes2.size() > 0)) {
            pagerType = PagerType.TYPE_INDEX;
            ListDataPresenter.changeSortStatus(false);
            return true;
        }
        if(sortNodes1.size() > 0 && sortNodes2.size() > 0){
            pagerType = PagerType.SORT_PAGER;
            return true;
        }
        pagerType = null;
        return false;
    }


    public enum PagerType {
        TYPE_INDEX, TYPE_SECOND,SORT_PAGER;
    }

}
