package com.bestar.accessapp.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bestar.accessapp.R;
import com.bestar.accessapp.util.AppSignUtil;
import com.bestar.accessapp.util.Click;
import com.bestar.accessapp.util.LFIoOps;
import com.bestar.accessapp.util.ServiceUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainPageInterface {
    public static int REQ_CODE_OVERLAY_PERMISSION = 0x0001;
    public static int REQ_CODE_ACCESSIBILITY_PERMISSION = 0x0002;

    private RelativeLayout mSwitchBar;
    private MainPresenter mMainPresenter;

    Process process = null;
    DataOutputStream os = null;
    DataInputStream is = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter = new MainPresenter(this);
        setContentView(R.layout.activity_main);
        setUpViews();
        //ServiceUtils.startJobService();
        getRoot();
        LFIoOps.initBaseAppFolder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("notification"))) {
            Toast.makeText(this, getIntent().getStringExtra("notification"), Toast.LENGTH_SHORT).show();
        }
        //TWindowManager.hideFloatBtn();
//        refreshDBItemCount();
        mMainPresenter.checkAccessibilityEnable();
    }


    @Override
    public void onClick(View v) {
        if (Click.isDblclick()) return;
        switch (v.getId()) {
            case R.id.switch_bar:
                break;
            case R.id.main_fab_btn:
                mMainPresenter.onMainFab();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.job_open:
                Timber.e("warningService-->" + "监听服务被开启");
                return true;
            case R.id.job_close:
                Timber.e("warningService-->" + "监听服务被关闭");
                ServiceUtils.closeJobService(this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.main_fab_btn).setOnClickListener(this);
        mSwitchBar = findViewById(R.id.switch_bar);
        mSwitchBar.setOnClickListener(this);
    }


    /***
     * 刷新无障碍权限的切换开关状态
     */
    @Override
    public void updateAccessibilitySwitchBar(boolean permission, boolean enable) {
        String statusStr = "Accessibility状态：" + (enable && permission ? "正常" : "停止");
        ((TextView) mSwitchBar.findViewById(R.id.status_text)).setText(statusStr);
        mSwitchBar.findViewById(R.id.accessibility_service_disabled_warning)
                .setVisibility(permission ? View.GONE : View.VISIBLE);
        ((Switch) mSwitchBar.findViewById(R.id.status_switch)).setOnCheckedChangeListener(
                mSwitchOnCheckedChangeListener);
        ((Switch) mSwitchBar.findViewById(R.id.status_switch)).setChecked(permission && enable);
    }

    private CompoundButton.OnCheckedChangeListener mSwitchOnCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            };

    private void getRoot() {
        try {
            process = Runtime.getRuntime().exec("/system/xbin/su");
            os = new DataOutputStream(process.getOutputStream());
            is = new DataInputStream(process.getInputStream());
            os.writeBytes("/system/bin/ls" + " \n");
            os.writeBytes(" exit \n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Timber.e("Unexpected error - Here is what I know:" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getAppSignInfo() {
        Timber.d("签名信息----" + AppSignUtil.getSign(getPackageManager(), getPackageName()));
    }
}
