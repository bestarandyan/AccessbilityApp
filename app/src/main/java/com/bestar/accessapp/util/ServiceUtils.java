package com.bestar.accessapp.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

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

	public static void closeJobService(Context context){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//21  5.0
			JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
			if (tm==null)return;
			tm.cancelAll();
		}
	}


}
