package com.bestar.accessapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bestar.accessapp.R;
import com.bestar.accessapp.accessibility.AccessibilityUtil;
import com.bestar.accessapp.util.Click;
import com.bestar.accessapp.util.LFIoOps;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView status_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        LFIoOps.initBaseAppFolder(this);
    }

    @Override
    public void onClick(View v) {
        if (Click.isDblclick()) return;
        switch (v.getId()) {
            case R.id.checkPermissionBtn:
                setAccessibilityPermission();
                break;
            case R.id.start58Btn:
                callWuBaApp("com.wuba");
                break;
            case R.id.startWechatBtn:
                callWuBaApp("com.tencent.mm");
                break;
            default:
                break;
        }
    }

    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.checkPermissionBtn).setOnClickListener(this);
        findViewById(R.id.start58Btn).setOnClickListener(this);
        findViewById(R.id.startWechatBtn).setOnClickListener(this);
        status_text = findViewById(R.id.status_text);
        status_text.setText(getStatusText());
    }

    private String getStatusText() {
        if (isSupportAccessibility()) {
            return "无障碍权限被开启";
        } else {
            return "无障碍权限被关闭";
        }
    }

    private boolean isSupportAccessibility() {
        return AccessibilityUtil.isEnableAccessibility(this);
    }

    private void setAccessibilityPermission() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, 1);
    }

    private void callWuBaApp(String appPackage) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackage);
            if (null == intent) return;
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toasty.error(this, "没有安装").show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        status_text.setText(getStatusText());
        super.onActivityResult(requestCode, resultCode, data);
    }
}
