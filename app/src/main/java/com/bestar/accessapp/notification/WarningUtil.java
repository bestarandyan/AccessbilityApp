package com.bestar.accessapp.notification;

import android.content.Context;
import android.media.MediaPlayer;

import com.bestar.accessapp.R;


/**
 * Created by lxx  on 2018/1/31
 */
public class WarningUtil {

    public MediaPlayer player;
    public static WarningUtil instance = null;

    public static WarningUtil getInstance() {
        if (null == instance){
            instance = new WarningUtil();
        }
        return instance;
    }

    public WarningUtil(){}

    public void playWarning(final Context context, final int resId) {
        if (player != null) {
            player.stop();
            player.reset();
            player = null;
        }
        player = MediaPlayer.create(context, resId);
        player.start();
    }

    public void completeListener(Context context) {
        playWarning(context, R.raw.audio_complete);
    }

    public void serviceStoped(Context context) {
        playWarning(context, R.raw.audio_service_stoped);
    }

    public void errorStoped(Context context) {
        playWarning(context, R.raw.audio_network_error);
    }

    public void serviceStart(Context context) {
        playWarning(context, R.raw.audio_start);
    }



}
