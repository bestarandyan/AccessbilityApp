package com.bestar.accessapp.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.bestar.accessapp.main.MainActivity;
import com.bestar.accessapp.main.MainActivity;
import com.bestar.accessapp.R;

/**
 * Created by lxx  on 2018/1/31
 */
public class NotificationUtil {

    public static void sendNotification(Context context,String content){
//        int mNotificationId = 001;
//        NotificationCompat.Builder mBuilder = getNotificationBuilder(context,"My notification title",content);
//        mBuilder.setContentIntent(getIntent(context));
//        Notification notification = mBuilder.build();
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(mNotificationId,notification);
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context,String title,String content){
       return new NotificationCompat.Builder(context,"accessapp")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setFullScreenIntent(getIntent(context),false)
                .setDefaults(Notification.DEFAULT_ALL);
    }

    public static PendingIntent getIntent(Context context){
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("notification","This is Notification!");
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }


}
