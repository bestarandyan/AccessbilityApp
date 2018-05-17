package com.bestar.accessapp.accessibility.presenter;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.accessibility.TAccessibilityInterface;
import com.bestar.accessapp.accessibility.TAccessibilityService;
import com.bestar.accessapp.notification.WarningUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailPresenter extends BasePresenter {
    private TAccessibilityInterface mInterface;

    public DetailPresenter(TAccessibilityInterface accessibilityInterface) {
        this.mInterface = accessibilityInterface;
    }

    private AccessibilityNodeInfo backNodeInfo;
    private AccessibilityNodeInfo callTitleNodeInfo;
    private AccessibilityNodeInfo cancelDialogNodeInfo;
    private AccessibilityNodeInfo specialCancelNodeInfo;
    private AccessibilityNodeInfo rentTxtPhoneNodeInfo;
    private AccessibilityNodeInfo rentPhoneNodeInfo;
    private AccessibilityNodeInfo priceNodeInfo;
    private AccessibilityNodeInfo houseTypeInfo;
    private AccessibilityNodeInfo spaceInfo;
    private AccessibilityNodeInfo titleInfo;

    private String phoneNum = "";
    private boolean hasClickPhone = false;
    private boolean hasProcessPhoneNum = false;
    private boolean isSpecialPage = false;
    private boolean hasClosePage = false;

    public void init() {
        backNodeInfo = null;
        callTitleNodeInfo = null;
        cancelDialogNodeInfo = null;
        specialCancelNodeInfo = null;
        rentTxtPhoneNodeInfo = null;
        rentPhoneNodeInfo = null;
        priceNodeInfo = null;
        houseTypeInfo = null;
        spaceInfo = null;
        titleInfo = null;
        phoneNum = "";
        hasClickPhone = false;
        hasProcessPhoneNum = false;
        isSpecialPage = false;
        hasClosePage = false;
    }


    public void onAccessibilityEvent(int eventType) {
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.e("andy", "窗口状态发生了改变");
                checkNeedsNodeInfo();
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.e("andy", "窗口内容发生了改变");
                //printfAllChild(mInterface.getRootNodeInfo());
                checkNeedsNodeInfo();
                break;
        }
    }

    /**
     * 处理窗口变化
     */
    private void checkNeedsNodeInfo() {
        processBaseInfo();
        processContentInfo();
        if (hasClickPhone) {
            processDialogInfo();
        }

        if (isErrorPage()) {
            if (!hasClosePage) {
                closeActivity();
            }
            return;
        }

        clickPhoneBtn();
        if (hasClickPhone) {
            processPhoneNumInfo();
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, 5000);
        }
    }

    private void processPhoneNumInfo() {
        getPhoneNum();
    }

    /**
     * 处理基础控件，返回按钮，电话按钮
     */
    private void processBaseInfo() {
        if (backNodeInfo == null) {
            backNodeInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/detail_top_bar_left_big_btn");
            if (backNodeInfo != null) {
                mHandler.sendEmptyMessageDelayed(1, 120000);
                Log.e("andy", "获取了-----返回按钮");
            } else {
                Log.e("andy", "获取返回按钮-----失败");
            }
        }

        if (rentPhoneNodeInfo == null) {
            rentPhoneNodeInfo = getRentPhoneNodeInfo();
            if (rentPhoneNodeInfo != null) {
                mHandler.removeMessages(1);
                Log.e("andy", "获取了-----电话按钮");
            } else {
                Log.e("andy", "获取电话按钮-----失败");
            }
        }
    }

    /**
     * 处理房源数据
     */
    private void processContentInfo() {
        if (titleInfo == null) {
            titleInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/title_text");
            if (titleInfo != null) {
                Log.e("andy", "获取了-----房源标题 = " + titleInfo.getText().toString());
            } else {
                Log.e("andy", "获取房源标题-----失败");
            }
        }

        if (priceNodeInfo == null) {
            priceNodeInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/price_text");
            if (priceNodeInfo != null) {
                Log.e("andy", "获取了-----房源价格 = " + priceNodeInfo.getText().toString());
            } else {
                Log.e("andy", "获取房源价格-----失败");
            }
        }

        if (houseTypeInfo == null) {
            houseTypeInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/house_type_text");
            if (houseTypeInfo != null) {
                Log.e("andy", "获取了-----房源户型 = " + houseTypeInfo.getText().toString());
            } else {
                Log.e("andy", "获取房源户型-----失败");
            }
        }

        if (spaceInfo == null) {
            spaceInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/space_text");
            if (spaceInfo != null) {
                Log.e("andy", "获取了-----房源面积");
            } else {
                Log.e("andy", "获取房源面积-----失败");
            }
        }
    }

    /**
     * 处理对话框里面的控件
     */
    private void processDialogInfo() {
        if (callTitleNodeInfo == null) {
            callTitleNodeInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/normal_call_title");
            if (callTitleNodeInfo != null) {
                Log.e("andy", "获取了-----电话标题");
            } else {
                Log.e("andy", "获取电话标题-----失败");
            }
        }

        if (cancelDialogNodeInfo == null) {
            cancelDialogNodeInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/normal_call_cancel");
            if (cancelDialogNodeInfo != null) {
                Log.e("andy", "获取了-----对话框返回按钮");
            } else {
                if (specialCancelNodeInfo == null) {
                    Log.e("andy", "获取对话框返回按钮-----失败");
                }
            }

        }

        if (specialCancelNodeInfo == null) {
            specialCancelNodeInfo = getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/cancel");
            if (specialCancelNodeInfo != null) {
                isSpecialPage = true;
                Log.e("andy", "获取了-----特殊页-----对话框返回按钮");
            } else {
                if (cancelDialogNodeInfo == null) {
                    Log.e("andy", "获取-----特殊页-----对话框返回按钮-----失败");
                }
            }
        }
    }

    /**
     * 处理特殊的房源，比如精品公寓,房源已下架
     */
    private boolean isErrorPage() {
        AccessibilityNodeInfo phoneBtn = getChildInfoByIdV2(mInterface.getRootNodeInfo(), "com.wuba:id/gongyu_tel");
        if (phoneBtn != null && (phoneBtn.getClassName().equals("android.widget.Button"))) {
            return true;
        }

        if (getChildInfoByIdV2(mInterface.getRootNodeInfo(), "com.wuba:id/house_detail_overdue_msg") != null) {
            Log.e("andy", "房源已下架");
            return true;
        }

        if (getChildInfoByIdV2(mInterface.getRootNodeInfo(), "com.wuba:id/warning_close") != null && backNodeInfo != null) {
            Log.e("andy", "弹出了警惕诈骗的警告");
            return true;
        }
        return false;
    }

    private void clickPhoneBtn() {
        if (titleInfo != null && callTitleNodeInfo == null && rentPhoneNodeInfo != null && !hasClickPhone) {
            rentPhoneNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            hasClickPhone = true;
            Log.e("andy", "点击了打电话按钮");
        }
    }

    private void getPhoneNum() {
        if (callTitleNodeInfo != null && TextUtils.isEmpty(phoneNum) && !hasProcessPhoneNum) {
            hasProcessPhoneNum = true;
            String nodeTxt = callTitleNodeInfo.getText().toString();
            if (!TextUtils.isEmpty(nodeTxt)) {
                String regEx = "[^0-9]";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(nodeTxt);
                phoneNum = matcher.replaceAll("").trim();
            }
            if (!hasClosePage) {
                closeActivity();
            }
        }

        if (specialCancelNodeInfo != null && TextUtils.isEmpty(phoneNum) && !hasProcessPhoneNum) {
            hasProcessPhoneNum = true;
            if (!hasClosePage) {
                closeActivity();
            }
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("andy", "房东设置了打电话时间");
                    if (!hasClosePage) {
                        closeActivity();
                    }
                    break;
                case 1:
                    Log.e("andy", "网络超时，等了2分钟没有返回数据");
                    WarningUtil.getInstance().errorStoped(BaseApp.getContext());
                    break;
            }
            return false;
        }
    });

    private void closeActivity() {
        hasClosePage = true;
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cancelDialogNodeInfo != null) {
                    Log.e("andy", "关闭对话框");
                    cancelDialogNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else if (specialCancelNodeInfo != null) {
                    Log.e("andy", "关闭特殊页面的对话框");
                    specialCancelNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }, 1000);

        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (backNodeInfo != null) {
                    ((TAccessibilityService) mInterface).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    if (isSpecialPage && specialCancelNodeInfo == null) {
                        Log.e("andy", "是特殊页面，但是 specialCancelNodeInfo == null");
                    }
                    Log.e("andy", "" + (specialCancelNodeInfo != null ? "特殊详情页关闭" : "详情页关闭"));
                    Log.e("andy", "===============================一条任务结束===============================");
                    mHandler.removeMessages(0);
                    DetailPresenter.this.init();
                }
            }
        }, 2000);
    }
    private AccessibilityNodeInfo getContentNodeInfo() {
        List<AccessibilityNodeInfo> nodeInfoList = null;
        AccessibilityNodeInfo contentNodeInfo = null;
        AccessibilityNodeInfo rootNodeInfo = mInterface.getRootNodeInfo();
        if (rootNodeInfo != null && rootNodeInfo.getChildCount() > 0) {
            nodeInfoList = rootNodeInfo.findAccessibilityNodeInfosByViewId(Window.ID_ANDROID_CONTENT + "");
        }

        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            contentNodeInfo = nodeInfoList.get(0);
        }
        return contentNodeInfo;
    }

    private void getChildInfoByContent(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo != null) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo childInfo = nodeInfo.getChild(i);
                if (childInfo != null && childInfo.getChildCount() > 0) {
                    getChildInfoByContent(childInfo, text);
                } else if (childInfo != null && childInfo.getText() != null) {
                    if (childInfo.getText().toString().equals(text)) {
                        rentTxtPhoneNodeInfo = childInfo;
                    } else {
                        getChildInfoByContent(childInfo, text);
                    }
                }
            }
        }
    }

    private AccessibilityNodeInfo getRentPhoneNodeInfo() {
        getChildInfoByContent(getContentNodeInfo(), "电话");
        if (rentTxtPhoneNodeInfo != null) {
            return rentTxtPhoneNodeInfo.getParent();
        }
        return null;
    }

    private List<AccessibilityNodeInfo> getAllChild(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        if (rootNodeInfo != null) {
            for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo childInfo = rootNodeInfo.getChild(i);
                if (childInfo != null) {
                    if (childInfo.getViewIdResourceName() != null) {
                        list.add(childInfo);
                    }
                    if (childInfo.getChildCount() > 1) {
                        list.addAll(getAllChild(childInfo));
                    }
                }
            }
        }
        return list;
    }


    private void printfAllChild(AccessibilityNodeInfo nodeInfo) {
        AccessibilityNodeInfo info = null;
        List<AccessibilityNodeInfo> list = getAllChild(nodeInfo);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getViewIdResourceName() != null) {
                Log.e("andy", "id = " + list.get(i).getViewIdResourceName());
            }
        }
    }


    private AccessibilityNodeInfo getChildInfoByIdV2(AccessibilityNodeInfo nodeInfo, String id) {
        AccessibilityNodeInfo info = null;
        List<AccessibilityNodeInfo> list = getAllChild(nodeInfo);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getViewIdResourceName().equals(id)) {
                info = list.get(i);
                break;
            }
        }
        return info;
    }

    @Override
    public boolean isMyEvent() {
        return (getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/detail_top_bar_left_big_btn") != null && getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/content_layout") != null) ||
                ((getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/other_call") != null || getChildInfoByIdV2(getContentNodeInfo(), "com.wuba:id/normal_call_title") != null) && backNodeInfo != null ||
                        backNodeInfo != null);
    }
}
