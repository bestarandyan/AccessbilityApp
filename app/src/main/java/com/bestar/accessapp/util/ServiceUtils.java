package com.bestar.accessapp.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.bestar.accessapp.BaseApp;
import com.bestar.accessapp.service.WarningJobService;

public class ServiceUtils {
	private static int JOB_ID = 1;
	/**
	 * 判断进程是否运行
	 * 
	 */
	public static boolean isProessRunning(Context context, String proessName) {

		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if (am == null)return false;
		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
			if (info.processName.equals(proessName)) {
				isRunning = true;
			}
		}

		return isRunning;
	}


	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public static boolean isJobServiceOn(Context context ) {
		JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
		boolean hasBeenScheduled = false ;
		if(null == scheduler)return false;
		for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
			if ( jobInfo.getId() == JOB_ID ) {
				hasBeenScheduled = true ;
				break ;
			}
		}
		return hasBeenScheduled ;
	}



	public static void startJobService1(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){//21  5.0
			JobScheduler jobScheduler = (JobScheduler) BaseApp.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
			ComponentName componentName = new ComponentName(BaseApp.getContext().getPackageName(), WarningJobService.class.getName());
			JobInfo jobInfo = new JobInfo.Builder(ServiceUtils.JOB_ID,componentName)
//                    .setPeriodic(100)
//                    .setPersisted(true)
					.setMinimumLatency(0)
					.setBackoffCriteria(0,JobInfo.BACKOFF_POLICY_LINEAR)
					.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
					.setOverrideDeadline(2000)
					.build();

			if(jobScheduler == null)return;
			jobScheduler.schedule(jobInfo);
		}
	}


	public static void closeJobService(Context context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//21  5.0
			JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
			if (tm==null)return;
			tm.cancelAll();
		}
	}


}
