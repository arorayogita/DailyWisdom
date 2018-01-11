package com.techindustan.dailywisdom.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.Constant;
import com.techindustan.dailywisdom.activity.MainActivity;
import com.techindustan.dailywisdom.activity.TodaysAudioActivity;
import com.techindustan.dailywisdom.utils.LineBarVisualizer;

/**
 * Created by android on 9/1/18.
 */

public class MusicService extends Service {
    private MediaPlayer player;
    public static boolean IS_SERVICE_RUNNING = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        player = MediaPlayer.create(MusicService.this, R.raw.song);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(Constant.ACTION.STARTFOREGROUND_ACTION)) {
          //  Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
            Log.i("tag", "Received Start Foreground Intent ");
            showNotification();
        } else if (intent.getAction().equals(Constant.ACTION.STOPFOREGROUND_ACTION)) {
          //  Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
            Log.i("tag", "Received Stop Foreground Intent");
            stopForeground(true);
            if (player != null) {
                player.stop();
            }
            stopSelf();

        } else if (intent.getAction().equals(Constant.ACTION.PLAY_ACTION)) {
            Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
            Log.e("player", "" + player.isPlaying() + "");
            if (!player.isPlaying()) {
                if (player != null) {
                    player.start();
                }
            }
        } else if (intent.getAction().equals(Constant.ACTION.PAUSE_ACTION)) {
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
            stopSelf();
            player.stop();

        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    private void showNotification() {
        player.start();
       // TodaysAudioActivity.lineBarVisualizer.setPlayer(player);
        MainActivity.lineBarVisualizer.setPlayer(player);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constant.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        RemoteViews notificationView = new RemoteViews(this.getPackageName(), R.layout.notification);
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(Constant.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_play, pplayIntent);
        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction(Constant.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_close, ppauseIntent);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Music Player")
                .setTicker("Music Player")
                .setContentText(" Music")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContent(notificationView)
                .setOngoing(true).build();


        startForeground(Constant.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }
}

