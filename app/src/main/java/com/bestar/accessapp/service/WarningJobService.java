package com.bestar.accessapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.accessibility.TAccessibilityUtil;
import com.bestar.accessapp.notification.NotificationUtil;
import com.bestar.accessapp.notification.WarningUtil;
import com.cocosw.favor.FavorAdapter;

/**
 * Created by lxx  on 2018/1/31
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class WarningJobService extends JobService {
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            boolean isRun = TAccessibilityUtil.isEnableAccessibility(WarningJobService.this);
            Log.e("warningService-->","监听服务监听结束===="+isRun);
            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);
            return true;
        }
    });
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Message m = Message.obtain();
        m.obj = jobParameters;
        handler.sendMessage(m);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        handler.removeCallbacksAndMessages(null);
        Log.e("warningService-->","监听服务被关闭");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

