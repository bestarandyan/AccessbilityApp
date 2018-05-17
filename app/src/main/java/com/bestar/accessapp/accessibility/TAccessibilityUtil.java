package com.bestar.accessapp.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import com.bestar.accessapp.other.Constants;

import java.util.List;

/**
 * Created by ted on 2018/4/17.
 * in com.tedxiong.helper.accessibility
 */
public class TAccessibilityUtil {

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }


    public static boolean isSupportAccessibility(Activity activity) {
        boolean result = false;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) activity.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (null == accessibilityManager) return false;
        List<AccessibilityServiceInfo> list =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : list) {
            if (info.getId().equals(activity.getPackageName() + Constants.ACCESSIBILITY_SERVICE_NAME))
                result = true;
        }
        return result;
    }

    public static boolean isEnableAccessibility(Context activity) {
        boolean result = false;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) activity.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (null == accessibilityManager) return false;
        List<AccessibilityServiceInfo> list =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : list) {
            if (info.getId().contains(Constants.ACCESSIBILITY_SERVICE_NAME))
                result = true;
        }
        return result;
    }
}
