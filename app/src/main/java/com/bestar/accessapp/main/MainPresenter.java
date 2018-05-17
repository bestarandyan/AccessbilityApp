/*
 *    Copyright 2016 Ted xiong-wei@hotmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.bestar.accessapp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.bestar.accessapp.accessibility.TAccessibilityUtil;
import com.cocosw.favor.FavorAdapter;
import com.bestar.accessapp.accessibility.TAccessibilityUtil;
import com.bestar.accessapp.R;
import com.bestar.accessapp.storage.SharePreTool;
import com.bestar.accessapp.util.VersionUtil;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ted on 2015/12/17.
 *
 * @ com.mrxiong.argot.main
 */
public class MainPresenter {
    private MainPageInterface mMainPageInterface;
    private SharePreTool mSharePreTool;
    private Context mContext;

    MainPresenter(MainPageInterface mainInterface) {
        this.mMainPageInterface = mainInterface;
        mContext = (Context) mainInterface;
        mSharePreTool = new FavorAdapter.Builder(mContext).build().create(SharePreTool.class);
    }

    public void checkAccessibilityEnable() {
        if (!mSharePreTool.isCheckDrawOverlays() && !canDrawOverlays()) {
            mSharePreTool.setCheckDrawOverlays(true);
            return;
        }
        if (!mSharePreTool.isCheckAccessibility() && !isSupportAccessibility()) {
            showAccessibilityDialog();
            return;
        }
        mMainPageInterface.updateAccessibilitySwitchBar(isSupportAccessibility(), mSharePreTool.getAppEnable());
    }

    /***
     * 更新switch bar 对应的数据
     *
     */
    public void onSwitchBarChange() {
        boolean isChecked = !mSharePreTool.getAppEnable();
        setAppEnable(isChecked);
    }

    public void setAppEnable(boolean enable) {
        if (enable) {
            mSharePreTool.setAppEnable(true);
            if (isSupportAccessibility()) {
                mMainPageInterface.updateAccessibilitySwitchBar(true, true);
            } else {
                showAccessibilityDialog();
            }
        } else {
            mSharePreTool.setAppEnable(false);
            mMainPageInterface.updateAccessibilitySwitchBar(isSupportAccessibility(), false);
        }
    }

    public void setJobServiceEnable(boolean enable){
        mSharePreTool.setAppEnable(enable);
    }

    public void onMainFab() {
        if (isSupportAccessibility()) {
            callWuBaApp();
        } else Toasty.error(mContext, "没有相关权限~~~~~").show();
    }

    /***
     * 检测是否支持windows显示
     */
    private boolean canDrawOverlays() {
        if (VersionUtil.isM()) {
            MainActivity mainActivity = (MainActivity) mMainPageInterface;
            if (!Settings.canDrawOverlays(mainActivity)) {
                showDrawOverlaysDialog();
                return false;
            }
        }
        return true;
    }

    /***
     * 设置浮窗权限
     */
    private void setDrawOverlays() {
        if (VersionUtil.isM()) {
            MainActivity mainActivity = (MainActivity) mMainPageInterface;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + mainActivity.getPackageName()));
            mainActivity.startActivityForResult(intent, MainActivity.REQ_CODE_OVERLAY_PERMISSION);
        }
    }

    private void setAccessibilityPermission() {
        MainActivity mainActivity = (MainActivity) mMainPageInterface;
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mainActivity.startActivityForResult(intent, MainActivity.REQ_CODE_ACCESSIBILITY_PERMISSION);
    }

    private boolean isSupportAccessibility() {
        MainActivity mainActivity = (MainActivity) mMainPageInterface;
        return TAccessibilityUtil.isEnableAccessibility(mainActivity);
//        return ServiceUtils.isServiceWork(mainActivity, Constants.ACCESSIBILITY_PATH);
    }

    private void showDrawOverlaysDialog() {
        new AlertDialog.Builder(mContext).setTitle(R.string.system_alter_permission_rationale_dialog_title)
                .setMessage(R.string.system_alert_permission_rationale_dialog_text)
                .setPositiveButton(R.string.open_settings, mDrawOverlaysDialogListener)
                .setNegativeButton(R.string.cancel, mDrawOverlaysDialogListener)
                .setCancelable(false)
                .show();
    }

    private void showAccessibilityDialog() {
        new AlertDialog.Builder(mContext).setTitle(R.string.enable_accessibility_dialog_title)
                .setMessage(R.string.enable_accessibility_service_message)
                .setPositiveButton(R.string.open_settings, mAccessibilityDialogListener)
                .setNegativeButton(R.string.cancel, mAccessibilityDialogListener)
                .setCancelable(false)
                .show();
    }


    private DialogInterface.OnClickListener mDrawOverlaysDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSharePreTool.setCheckDrawOverlays(true);
                    if (which == -1) {
                        setDrawOverlays();
                    } else {
                        mMainPageInterface.updateAccessibilitySwitchBar(isSupportAccessibility(), mSharePreTool.getAppEnable());
                    }
                }
            };

    private DialogInterface.OnClickListener mAccessibilityDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSharePreTool.setCheckAccessibility(true);
                    if (which == -1) {
                        setAccessibilityPermission();
                    } else {
                        mMainPageInterface.updateAccessibilitySwitchBar(isSupportAccessibility(), mSharePreTool.getAppEnable());
                    }
                }
            };


    private void callWuBaApp() {
        try {
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.wuba");
            if (null == intent) return;
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toasty.error(mContext, "没有安装").show();
        }
    }

}
