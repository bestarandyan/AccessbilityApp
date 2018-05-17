package com.bestar.accessapp.util;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by ted on 2018/4/24.
 * in com.bestar.accessapp.util
 */
public class LogEvent {
    private static final String TAG = "LogEvent";
    public static void LOG(AccessibilityEvent event) {
        int eventType = event.getEventType();

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.i(TAG, "eventType :AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                Log.i(TAG, "eventType :AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED");
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "eventType :AccessibilityEvent.TYPE_VIEW_CLICKED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.i(TAG, "eventType :AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.i(TAG, "eventType :AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED");
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.i(TAG, "eventType :AccessibilityEvent.TYPE_VIEW_SCROLLED");
                break;
            default:
                Log.i(TAG, "eventType :default type ==" + eventType);
                break;
        }
    }
}
