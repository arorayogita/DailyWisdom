package com.techindustan.dailywisdom.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.MainActivity;
import com.techindustan.dailywisdom.utils.Constant;
import com.techindustan.dailywisdom.utils.SongsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by android on 17/1/18.
 */

public class MusicBoundService extends Service implements MediaPlayer.OnCompletionListener {
    private MediaPlayer player = new MediaPlayer();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    int songIndex = -1;
    public static boolean IS_SERVICE_RUNNING = false;

    private IBinder mBinder = new MyBinder();
    private int length = 0;


    @Override
    public void onCreate() {
        player.setOnCompletionListener(this);
        IS_SERVICE_RUNNING = true;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (intent.getAction().equals(Constant.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i("tag", "Received Start Foreground Intent ");
            showNotification();
            playSong(songIndex);

        } else if (intent.getAction().equals(Constant.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i("tag", "Received Stop Foreground Intent");
            stopForeground(true);
            if (player != null) {
                player.stop();
            }
            stopSelf();


        } else*/
        /*if (intent.getAction().equals(Constant.ACTION.PLAY_ACTION)) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.seekTo(length);
                    player.start();
                }
            }
        } else if (intent.getAction().equals(Constant.ACTION.PAUSE_ACTION)) {
            player.stop();
            player.release();
            stopSelf();

        }*/
        return START_STICKY;

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    public void setIndex(int index) {
        songIndex = index;
    }

    public boolean isMediaPlaying() {
        return player != null && player.isPlaying();
    }

    public int getProgress() {
        if (player != null && player.isPlaying()) {
            player.getCurrentPosition();
            return player.getCurrentPosition();
        }
        return 0;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setSongList(ArrayList<HashMap<String, String>> songs) {
        songsList = songs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_SERVICE_RUNNING = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.reset();
        return false;
    }

    public void resetPlayer() {
        length = 0;
        player.stop();
        player.reset();
    }
    public int getDuration()
    {
        if(player!=null&&player.isPlaying())
        {
            return player.getDuration();
        }
        return 0;
    }


    public void playSong(int songIndex) {
        try {
            if (player != null) {
                /*if (player.isPlaying()) {
                    player.seekTo(length);
                    //player.prepare();
                    player.start();
                } else {*/
                if (length > 0) {
                    player.seekTo(length);
                    player.start();
                } else {
                    player.setDataSource(songsList.get(songIndex).get("songPath"));
                    player.prepare();
                    player.start();
                }
                // }
            } else {
                Toast.makeText(this, "Starting player please wait..", Toast.LENGTH_SHORT).show();
            }
            Log.e("path", "" + songsList.get(songIndex).get("songPath"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void pauseAudio() {
        if (player.isPlaying()) {
            player.pause();
            length = player.getCurrentPosition();
        }
    }

    public void playNext() {
        resetPlayer();
        // check if next song is there or not
        if (songsList.size() > 0 && songIndex < songsList.size() - 1) {
            songIndex = songIndex + 1;
            playSong(songIndex);
        } else {
            resetPlayer();
            playSong(songIndex);
            Toast.makeText(this, "End of the playlist", Toast.LENGTH_SHORT).show();
        }

    }

    public int getIndex() {
        return songIndex;
    }


    public void playPrev() {
        resetPlayer();
        if (songsList.size() > 0 && songIndex > 0) {
            songIndex--;
            playSong(songIndex);
        } else {
            resetPlayer();
            playSong(songIndex);
            Toast.makeText(this, "Already at start of the playlist", Toast.LENGTH_SHORT).show();
        }


    }

    public String getSongName() {
        String songName;
        songName = songsList.get(songIndex).get("songTitle");
        return songName;
    }

    public MediaPlayer setVisvulization() {
        return player;
    }

    public class MyBinder extends Binder {
        public MusicBoundService getService() {
            return MusicBoundService.this;
        }

    }


    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constant.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        RemoteViews notificationView = new RemoteViews(this.getPackageName(), R.layout.notification);
        Intent playIntent = new Intent(this, MusicBoundService.class);
        playIntent.setAction(Constant.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_button_play, pplayIntent);
        Intent pauseIntent = new Intent(this, MusicBoundService.class);
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
