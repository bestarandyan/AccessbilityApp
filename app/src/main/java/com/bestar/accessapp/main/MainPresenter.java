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
import android.support.v7.app.AlertDialog;

import com.bestar.accessapp.accessibility.AccessibilityUtil;
import com.bestar.accessapp.R;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ted on 2015/12/17.
 *
 * @ com.mrxiong.argot.main
 */
public class MainPresenter {
    private MainPageInterface mMainPageInterface;
    private Context mContext;

    MainPresenter(MainPageInterface mainInterface) {
        this.mMainPageInterface = mainInterface;
        mContext = (Context) mainInterface;
    }

    public void checkAccessibilityEnable() {

    }

    public void onMainFab() {
        if (isSupportAccessibility()) {
            callWuBaApp();
        } else Toasty.error(mContext, "没有相关权限~~~~~").show();
    }

    /***
     * 更新switch bar 对应的数据
     *
     */
    public void onSwitchBarChange(boolean isChecked) {
        setAppEnable(isChecked);
    }
    public void setAppEnable(boolean enable) {
        if (enable) {
            if (isSupportAccessibility()) {
                mMainPageInterface.updateAccessibilitySwitchBar(true, true);
            } else {
                showAccessibilityDialog();
            }
        } else {
            mMainPageInterface.updateAccessibilitySwitchBar(isSupportAccessibility(), false);
        }
    }

    private void showAccessibilityDialog() {
        new AlertDialog.Builder(mContext).setTitle(R.string.enable_accessibility_dialog_title)
                .setMessage(R.string.enable_accessibility_service_message)
                .setPositiveButton(R.string.open_settings, mAccessibilityDialogListener)
                .setNegativeButton(R.string.cancel, mAccessibilityDialogListener)
                .setCancelable(false)
                .show();
    }

    private DialogInterface.OnClickListener mAccessibilityDialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (which == -1) {
                    } else {
                        mMainPageInterface.updateAccessibilitySwitchBar(isSupportAccessibility(), true);
                    }
                }
            };

    private boolean isSupportAccessibility() {
        MainActivity mainActivity = (MainActivity) mMainPageInterface;
        return AccessibilityUtil.isEnableAccessibility(mainActivity);
//        return ServiceUtils.isServiceWork(mainActivity, Constants.ACCESSIBILITY_PATH);
    }

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
