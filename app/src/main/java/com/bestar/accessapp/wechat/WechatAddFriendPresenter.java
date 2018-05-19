package com.bestar.accessapp.wechat;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.accessibility.AccessibilityInterface;
import com.bestar.accessapp.accessibility.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxx  on 2018/1/31
 */
public class WechatAddFriendPresenter extends BasePresenter {
    AccessibilityInterface mInterface;

    public WechatAddFriendPresenter(AccessibilityInterface interfaces){
        this.mInterface = interfaces;
    }
    @Override
    public boolean isMyEvent() {
        return (getChildInfoById(getContentNodeInfo(), "com.tencent.mm:id/hx") != null);
    }


    private AccessibilityNodeInfo getContentNodeInfo() {
        List<AccessibilityNodeInfo> nodeInfoList = null;
        AccessibilityNodeInfo contentNodeInfo = null;
        AccessibilityNodeInfo rootNodeInfo = mInterface.getNodeInfo();
        if (rootNodeInfo != null && rootNodeInfo.getChildCount() > 0) {
            nodeInfoList = rootNodeInfo.findAccessibilityNodeInfosByViewId(Window.ID_ANDROID_CONTENT + "");
        }

        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            contentNodeInfo = nodeInfoList.get(0);
        }
        return contentNodeInfo;
    }


    private AccessibilityNodeInfo getChildInfoById(AccessibilityNodeInfo nodeInfo, String id) {
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

    public void inputEt(){
        AccessibilityNodeInfo inputInfo = getChildInfoById(getContentNodeInfo(),"com.tencent.mm:id/c9y");
        if (inputInfo!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "18616190649");
                inputInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
        }
    }

    public void clickInput(){
        AccessibilityNodeInfo inputInfo = getChildInfoById(getContentNodeInfo(),"com.tencent.mm:id/ij");
        if (inputInfo!=null){
            inputInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
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

}
