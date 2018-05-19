package com.bestar.accessapp.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.bestar.accessapp.wechat.WechatAddFriendPresenter;

/**
 * Created by lxx  on 2018/1/31
 */
public class AccessibilityAppService extends AccessibilityService implements AccessibilityInterface{
    WechatAddFriendPresenter mWechatPresenter;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mWechatPresenter = new WechatAddFriendPresenter(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int type = accessibilityEvent.getEventType();
        Log.e("accessibilityEvent====>",type+"");
        if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            onEvent(type);
        }
    }

    private void onEvent(int eventType){
        if (mWechatPresenter!=null && mWechatPresenter.isMyEvent()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWechatPresenter.clickInput();
                }
            },1000);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public AccessibilityNodeInfo getNodeInfo() {
        return getRootInActiveWindow();
    }
}
