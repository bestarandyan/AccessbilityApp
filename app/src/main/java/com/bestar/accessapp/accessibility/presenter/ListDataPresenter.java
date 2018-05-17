package com.bestar.accessapp.accessibility.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.accessibility.TAccessibilityService;
import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.accessibility.TAccessibilityService;
import com.bestar.accessapp.notification.WarningUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User:chenzhe
 * Date: 2018/4/18
 * Time:10:37
 */
public class ListDataPresenter extends BasePresenter {
    private static final String TAG = ListDataPresenter.class.getSimpleName();
    private TAccessibilityInterface mInterface;
    public static List<String> itemClicks = Collections.synchronizedList(new ArrayList<String>());
    public static ListDataPresenter mListDataPresenter;

    public static String TOTAL_NUM = "total_num";
    private boolean isScroll = false;
//    private final int listSize = 200;//集合长度

    public static ListDataPresenter getIntance(TAccessibilityInterface tInterface) {
        if (mListDataPresenter == null) {
            synchronized (ListDataPresenter.class) {
                if (mListDataPresenter == null) {
                    mListDataPresenter = new ListDataPresenter();
                }
            }
        }
        mListDataPresenter.setTAccessibilityInterface(tInterface);
        return mListDataPresenter;
    }

    private DelayHandler mDelayHandler = new DelayHandler(this);

    private void setTAccessibilityInterface(TAccessibilityInterface tInterface) {
        mInterface = tInterface;
    }
   /* public ListDataPresenter(TAccessibilityInterface tInterface) {
        mInterface = tInterface;
    }*/

    public static boolean isFlit = false;

    /***
     * 处理无障碍消息事件
     *
     */
    public void onHandleAccessibilityEvent(int eventType) {
        setCity();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (!isFlit) {
                    judgeFlit();
                } else {
                    startTask();
                }
                break;
        }
    }

    /**
     * 设置城市名称
     */
    private void setCity() {
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if(rootNodeInfo == null){
            return;
        }
        final List<AccessibilityNodeInfo> btnSortNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/filter_cate_one");//城市名称
        if (btnSortNodes.size() > 0 && btnSortNodes.get(0).getText() != null) {
            String city = btnSortNodes.get(0).getText().toString().replace("全", "");
            if (!city.equals(TAccessibilityService.getCurrentCity())) {
                TAccessibilityService.setCurrentCity(city);
            }
        }
    }

    /**
     * 判断筛选
     */
    public void judgeFlit() {
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if(rootNodeInfo == null){
            return;
        }
        final List<AccessibilityNodeInfo> btnSortNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/btn_sort");//点击排序按钮
        if (btnSortNodes.size() > 0) {
            btnSortNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    /**
     * 开始执行操作
     */
    private void startTask() {
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        final List<AccessibilityNodeInfo> titlesNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/new_version_title");

        if (titlesNodes.size() > 0) {
            Log.i(TAG, "当前列表item节点数" + titlesNodes.size());
            if (isScroll) { //刚翻过页
                isScroll = false;
                mDelayHandler.sendEmptyMessageDelayed(0, 1000);//滚动之后延迟点击流程
            } else {
                cycleClick(rootNodeInfo, titlesNodes);//正常点击流程
            }
        } else {
            Log.i(TAG, "当前列表无节点");
        }

    }

    int count = 0;

    private void cycleClick(AccessibilityNodeInfo rootNodeInfo, List<AccessibilityNodeInfo> titlesNodes) {
        boolean isAllClick = true;
        for (int i = 0; i < titlesNodes.size(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = titlesNodes.get(i);
            String title = accessibilityNodeInfo.getText().toString();
            AccessibilityNodeInfo timeNode = findTimeNode(accessibilityNodeInfo);


            if (!itemClicks.contains(title)) {//说明没有点击过
                if ((timeNode != null && !TextUtils.isEmpty(timeNode.getText()) && (timeNode.getText().toString().endsWith("天前")))) {
                    //任务完成
                    taskCompleted();
                }
                isAllClick = false;
                titlesNodes.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    /*if(itemClicks.size() > listSize){//保证集合的长度
                        itemClicks.remove(0);-+*
                    }*/
                itemClicks.add(title);
                Log.w(TAG, title + "已处理");
                break;

                //爬最大数目长度
              /*  itemClicks.add(title);
                count ++ ;
                Logger.wtf("运行数据"+ count);
                Logger.w(title + "已处理");*/

            }

        }
        if (isAllClick) {//说明全部点击过了
//            ShareDataUtil.putString(TOTAL_NUM, count + "(未完成)");
            List<AccessibilityNodeInfo> noMoreNode = rootNodeInfo.findAccessibilityNodeInfosByText("没有更多信息了");
            if (noMoreNode.size() > 0) {
                //任务完成
                taskCompleted();
                return;
            }
            final List<AccessibilityNodeInfo> listViewNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/list_data_list");//listView的节点
            if (listViewNodes != null && listViewNodes.size() != 0) {
                listViewNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                isScroll = true;
                startTask();
            }
        }
    }

    /**
     * 任务完成
     */
    private void taskCompleted() {
        Log.w(TAG, "一天的数据已跑完，任务完成");
        Toast.makeText(BaseApp.getContext(), "任务完成", Toast.LENGTH_LONG).show();
        WarningUtil.getInstance().completeListener(BaseApp.getContext());
        return;
    }

    private AccessibilityNodeInfo findTimeNode(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
            if (parent != null) {
                List<AccessibilityNodeInfo> timeNodes = parent.findAccessibilityNodeInfosByViewId("com.wuba:id/item_date");
                if (timeNodes.size() > 0) {
                    return timeNodes.get(0);
                }
            }
          /*  if(parent != null && parent.getChildCount() > 2){
                AccessibilityNodeInfo child = parent.getChild(2);
                if(child != null && child.getChildCount() > 1){
                    AccessibilityNodeInfo child2 = child.getChild(1);
                    if(child2!= null && !TextUtils.isEmpty(child2.getText()) && child2.getText()){
                    }
                }
            }*/
        }
        return null;
    }

    public static void changeSortStatus(boolean isSort) {
        isFlit = isSort;
    }

    /**
     * 延迟消息滚动，防止还没滚动完就开始点击
     */
    static class DelayHandler extends Handler {
        WeakReference<ListDataPresenter> mListDataPresenterWeakReference;

        public DelayHandler(ListDataPresenter listDataPresenter) {
            mListDataPresenterWeakReference = new WeakReference<ListDataPresenter>(listDataPresenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                ListDataPresenter listDataPresenter = mListDataPresenterWeakReference.get();
                if (listDataPresenter != null) {
                    listDataPresenter.startTask();
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

        final List<AccessibilityNodeInfo> listViewNodes = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/list_data_list");//listView的节点
        final List<AccessibilityNodeInfo> btnSort = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.wuba:id/btn_sort");// 点击排序按钮
        Log.i(TAG, "listViewNodes.size" + listViewNodes.size() + "btnSort.size" + btnSort.size());
        if (listViewNodes.size() != 0 && btnSort.size() > 0) {
            return true;
        }

        return false;
    }
}
