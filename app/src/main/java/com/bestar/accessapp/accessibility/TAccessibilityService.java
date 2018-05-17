package com.bestar.accessapp.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.accessibility.presenter.DetailPresenter;
import com.bestar.accessapp.accessibility.presenter.ErrorPresenter;
import com.bestar.accessapp.accessibility.presenter.ListDataPresenter;
import com.bestar.accessapp.accessibility.presenter.OtherSkipPresenter;
import com.bestar.accessapp.notification.NotificationUtil;
import com.bestar.accessapp.notification.WarningUtil;
import com.bestar.accessapp.util.LogEvent;

import timber.log.Timber;

/**
 * Created by ted on 2018/4/17.
 * in com.tedxiong.helper.accessibility
 */
public class TAccessibilityService extends AccessibilityService implements TAccessibilityInterface {
    private final static int MSG_TYPE_WINDOW_CONTENT_CHANGED = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
    private final static int MSG_TYPE_WINDOW_STATE_CHANGED = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
    private final static int EVENT_DISPOSE_TIME = 1000;
    private static final int IS_HAVE_NODE = 0X006;
    private ListDataPresenter mListDataPresenter;
    private ErrorPresenter mErrorPresenter;
    private DetailPresenter mDetailPresenter;
    private static String currentCity = "上海";
    private boolean isHaveNode = false;
    public static String getCurrentCity() {
        return currentCity;
    }

    public static void setCurrentCity(String currentCity) {
        TAccessibilityService.currentCity = currentCity;
    }

    @Override
    public AccessibilityNodeInfo getRootNodeInfo() {
        return getRootInActiveWindow();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        LogEvent.LOG(event);
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (mHandler.hasMessages(MSG_TYPE_WINDOW_CONTENT_CHANGED)) {
                mHandler.removeMessages(MSG_TYPE_WINDOW_CONTENT_CHANGED);
            }
            Message message = new Message();
            message.what = MSG_TYPE_WINDOW_CONTENT_CHANGED;
            mHandler.sendMessageDelayed(message, EVENT_DISPOSE_TIME);
            Log.d("xiongwei", "++++++++++++++++发送消息~~~~~" + message.getWhen());
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (mHandler.hasMessages(MSG_TYPE_WINDOW_STATE_CHANGED)) {
                mHandler.removeMessages(MSG_TYPE_WINDOW_STATE_CHANGED);
            }
            Message message = new Message();
            message.what = MSG_TYPE_WINDOW_STATE_CHANGED;
            mHandler.sendMessageDelayed(message, EVENT_DISPOSE_TIME);
        }

//        if (!ServiceUtils.isJobServiceOn(BaseApp.getContext())){
//            Log.e("warningService-->","-------------守护进程被终止------------");
//            if ( mSharePreTool.getJobServiceEnable()) {
//               ServiceUtils.startJobService();
//            }
//        }
    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TYPE_WINDOW_STATE_CHANGED:
                    splitEvent(msg);
                    break;
                case MSG_TYPE_WINDOW_CONTENT_CHANGED:
                    splitEvent(msg);
                    break;
                case IS_HAVE_NODE:
                    if(!isHaveNode){
                        performGlobalAction(GLOBAL_ACTION_BACK);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private void splitEvent(Message msg) {
        Log.d("xiongwei", "----------------------------处理消息~~~~~" + msg.getWhen() + "-------type == " + msg.what);
        onActionEvent(msg.what);
    }

    private void onActionEvent(int eventType) {
        if (mErrorPresenter != null && mErrorPresenter.isMyEvent()) {
            mErrorPresenter.onHandleAccessibilityEvent(eventType);
            Timber.w("匹配到错误页面 , time:" + SystemClock.currentThreadTimeMillis());
            mDetailPresenter.init();
            isHaveNode = true;
        } else if (OtherSkipPresenter.getInstance(this) != null && OtherSkipPresenter.getInstance(this).isMyEvent()) {
            OtherSkipPresenter.getInstance(this).onHandleAccessibilityEvent(eventType);
            Timber.w("匹配到其他自动跳过的页面 , time:" + SystemClock.currentThreadTimeMillis());
            isHaveNode = true;
        } else if (mListDataPresenter != null && mListDataPresenter.isMyEvent()) {
            mListDataPresenter.onHandleAccessibilityEvent(eventType);
            Timber.w("匹配到List页面 , time:" + SystemClock.currentThreadTimeMillis());
            mDetailPresenter.init();
            isHaveNode = true;
        } else if (mDetailPresenter != null && mDetailPresenter.isMyEvent()) {
            mDetailPresenter.onAccessibilityEvent(eventType);
            Timber.w("匹配到详情页面 , time:" + SystemClock.currentThreadTimeMillis());
            isHaveNode = true;
        } else {
            isHaveNode = false;
            Timber.w("没匹配到节点，返回上一级 , time:" + SystemClock.currentThreadTimeMillis());
            mHandler.sendEmptyMessageDelayed(IS_HAVE_NODE,3000);
        }
    }






    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动GuardService
     */
    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onInterrupt() {
        NotificationUtil.sendNotification(getApplicationContext(), "监听服务已被终止");
        WarningUtil.getInstance().serviceStoped(BaseApp.getContext());
        Log.e("onInterrupt", "Service is interrupt");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind", "Service is onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mListDataPresenter = ListDataPresenter.getIntance(this);
        mErrorPresenter = ErrorPresenter.getIntance(this);
        mDetailPresenter = new DetailPresenter(this);
    }


    @Override
    public void onDestroy() {
        NotificationUtil.sendNotification(getApplicationContext(), "监听服务已被终止");
        WarningUtil.getInstance().serviceStoped(BaseApp.getContext());
        Log.e("destroy", "Service is destroy");
        super.onDestroy();
    }
}
