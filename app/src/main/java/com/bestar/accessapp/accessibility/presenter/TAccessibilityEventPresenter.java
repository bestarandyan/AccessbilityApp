package com.bestar.accessapp.accessibility.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.model.RecordItem;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ted on 2018/4/17.
 * in com.tedxiong.helper.accessibility
 */
public class TAccessibilityEventPresenter {
    private TAccessibilityInterface mInterface;

    public TAccessibilityEventPresenter(TAccessibilityInterface tInterface) {
        mInterface = tInterface;
    }

    /***
     * 处理无障碍消息事件
     *
     * @param event 消息事件
     */
    public void onHandleAccessibilityEvent(AccessibilityEvent event) {
        if (!isListenApp(event)) return;
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.d("eventType", "AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                Log.d("eventType", "AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d("eventType", "AccessibilityEvent.TYPE_VIEW_CLICKED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d("eventType", "AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
                findPhoneTxt(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d("eventType", "AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.d("eventType", "AccessibilityEvent.TYPE_VIEW_SCROLLED");
                break;
            default:
                Log.d("eventType", "default type ==" + eventType);
                break;
        }
    }


    /*************************************/
    private void findPhoneTxt(AccessibilityEvent event) {
        for (int j=0;j<event.getRecordCount();j++) {
            Log.e("before_record",event.getRecord(j).getBeforeText().toString());
        }
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if (null == rootNodeInfo) return;
        List<AccessibilityNodeInfo> callTxtNodeList = rootNodeInfo.findAccessibilityNodeInfosByText("是否呼叫");
        for(int i=0;i<rootNodeInfo.getChildCount();i++){
            AccessibilityNodeInfo view = rootNodeInfo.getChild(i);
            Log.e("viewType=======",view.getClassName().toString());
            if (view.getClassName().equals("android.widget.TextView")){
                Log.e(view.getClassName()+"=====",view.getText()!=null?view.getText().toString():"");
            }
        }
        if (null == callTxtNodeList || callTxtNodeList.size() == 0)
            callTxtNodeList = rootNodeInfo.findAccessibilityNodeInfosByText("使用");
        if (null != callTxtNodeList && callTxtNodeList.size() > 0) {
            actionPhoneTxt(callTxtNodeList);
            openPacket(rootNodeInfo.findAccessibilityNodeInfosByText("呼叫").get(1));
        }
    }

    private void actionPhoneTxt(List<AccessibilityNodeInfo> callTxtNodeList) {
        for (AccessibilityNodeInfo nodeInfo : callTxtNodeList) {
            if (null == nodeInfo) continue;
            String nodeTxt = nodeInfo.getText().toString();
            Log.d("xiongwei", "____________解析内容是：" + nodeTxt);
            if (TextUtils.isEmpty(nodeTxt)) continue;
            String regEx = "[^0-9]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(nodeTxt);
            String result = matcher.replaceAll("").trim();
            Log.d("xiongwei", "____________获取到的电话号码是：" + result);
            if (!TextUtils.isEmpty(result)) {
                RecordItem item = new RecordItem();
                item.setHousePhone(result);
            }
        }
    }

    private void openPacket(AccessibilityNodeInfo info) {
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float dpi = metrics.densityDpi;
        if (android.os.Build.VERSION.SDK_INT <= 23) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            if (android.os.Build.VERSION.SDK_INT > 23) {
//                Path path = new Path();
//                if (640 == dpi) { //1440
//                    path.moveTo(720, 1575);
//                } else if(320 == dpi){//720p
//                    path.moveTo(355, 780);
//                }else if(480 == dpi){//1080p
//                    path.moveTo(533, 1115);
//                }
//                GestureDescription.Builder builder = new GestureDescription.Builder();
//                GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 450, 50)).build();
//                mInterface.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
//                    @Override
//                    public void onCompleted(GestureDescription gestureDescription) {
//                        mMutex = false;
//                        super.onCompleted(gestureDescription);
//                    }
//
//                    @Override
//                    public void onCancelled(GestureDescription gestureDescription) {
//                        mMutex = false;
//                        super.onCancelled(gestureDescription);
//                    }
//                }, null);

            }
        }
    }

    private void readTextChangeEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) return;
        AccessibilityNodeInfo focusNodeInfo = getEditTextNodeInfo(source);
        if (null != focusNodeInfo) {
            final String text = focusNodeInfo.getText() == null ? "" : focusNodeInfo.getText().toString();
            if (TextUtils.isEmpty(text)) return;
            Log.d("xiongwei", "____________phoneNumber ===" + text);
        }
    }

    private AccessibilityNodeInfo getEditTextNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (true) {
            if (current == null) {
                return null;
            }
            if ("android.widget.EditText".equals(current.getClassName().toString())) {
                return current;
            }
            AccessibilityNodeInfo oldCurrent = current;
            current = oldCurrent.getParent();
            oldCurrent.recycle();
        }
    }


    private boolean isListenApp(AccessibilityEvent event) {
        if (null == event) return false;
        String packageName = event.getPackageName().toString();
        if (TextUtils.isEmpty(packageName)) return false;
        if (packageName.equals("com.tedxiong.helper") ||
                packageName.equals("com.android.contacts") ||
                packageName.equals("com.wuba")) {
            return true;
        }
        return false;
    }

//    private String getPackageName() {
//        String packageName = mContext.getPackageName();
//        if (TextUtils.isEmpty(packageName)) packageName = "com.tedxiong.helper";
//        return packageName;
//    }
}
