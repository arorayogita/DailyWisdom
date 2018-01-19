package com.techindustan.dailywisdom.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.adapter.AudioBackgroundAdapter;
import com.techindustan.dailywisdom.adapter.AudioHistoryAdapter;
import com.techindustan.dailywisdom.adapter.CarouselPagerAdapter;
import com.techindustan.dailywisdom.fragment.AudioHistoryFragment;
import com.techindustan.dailywisdom.fragment.SettingsFragment;
import com.techindustan.dailywisdom.service.MusicBoundService;
import com.techindustan.dailywisdom.utils.Constant;
import com.techindustan.dailywisdom.utils.LineBarVisualizer;
import com.techindustan.dailywisdom.utils.SongsManager;
import com.techindustan.dailywisdom.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements AudioHistoryAdapter.SonglistListener, ViewPager.OnPageChangeListener {


    private static final int SHOW_PROGRESS = 1;
    @BindView(R.id.ivPreviousAudioMessages)
    ImageView ivPreviousAudioMessages;
    @BindView(R.id.ivSettings)
    ImageView ivSettings;
    @BindView(R.id.ivPrev)
    ImageView ivPrev;
    @BindView(R.id.ivNext)
    ImageView ivNext;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.myViewPager)
    public ViewPager myViewPager;
    @BindView(R.id.tvSongTitle)
    TextView tvSongTitle;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.visualizer)
    LineBarVisualizer visualizer;
    @BindView(R.id.tvSongDuration)
    TextView tvSongDuration;
    @BindView(R.id.ivPlay)
    ImageView ivPlay;
    private String downloadAudioPath;
    private String urlDownloadLink = "";
    String filename;
    String type;
    public CarouselPagerAdapter adapter;
    public static int count = 3;
    public static int FIRST_PAGE = 3;
    int songIndex = 0;
    MusicBoundService mBoundService = null;
    boolean isServiceBound = false;
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    SongsManager songManager;
    boolean isPlaying = false;
    AudioBackgroundAdapter cardsPagerAdapter;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "newVoices");
        if (!audioVoice.exists()) {
            audioVoice.mkdir();
        }

        songManager = new SongsManager();
        if (songManager.getPlayList().size() > 0) {
            songsList.add(songManager.getPlayList().get(0));
        }



/*        Log.e("songIndex", "" + songIndex);
         player = MediaPlayer.create(MainActivity.this, R.raw.red_e);
        tvSongDuration.setText(Utilities.milliSecondsToTimer(player.getDuration()));*/

        myViewPager.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 4) * 2);
        myViewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -2);
        cardsPagerAdapter = new AudioBackgroundAdapter(songsList, this);
        myViewPager.setAdapter(cardsPagerAdapter);
        cardsPagerAdapter.notifyDataSetChanged();
        myViewPager.setOffscreenPageLimit(3);
        myViewPager.addOnPageChangeListener(this);
        urlDownloadLink = "https://mp3oye.com/stream/20bbc370c9872271f2b1df1350599bee";
        SongsManager songsManager = new SongsManager();
        filename = songsManager.extractFilename(urlDownloadLink);
      /*  downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
        SongsManager.DownloadMedia downloadAudioFile = new SongsManager.DownloadMedia();
        downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);*/

        Intent intent = new Intent(this, MusicBoundService.class);
        intent.setAction(Constant.ACTION.STARTFOREGROUND_ACTION);
        if (isMyServiceRunning(MusicBoundService.class)) {
            startService(intent);
        }
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);


    }
/*
    @Override
    protected void onStop() {
        super.onStop();
        visualizer.release();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (isServiceBound) {
            if (mBoundService.isMediaPlaying()) {
                isPlaying = true;
                songIndex = mBoundService.getIndex();
                ivPlay.setImageResource(R.drawable.pause_icon);
            } else {
                isPlaying = false;
                ivPlay.setImageResource(R.drawable.play_icon);
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBoundService.MyBinder myBinder = (MusicBoundService.MyBinder) service;
            mBoundService = myBinder.getService();
            isServiceBound = true;
            startVisualization(mBoundService.getPlayer());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBoundService != null) {
            if (!mBoundService.isMediaPlaying()) {
                Intent intent = new Intent(this, MusicBoundService.class);
                stopService(intent);
            }
        }

    }


    @OnClick({R.id.ivPreviousAudioMessages, R.id.ivSettings, R.id.ivPlay, R.id.ivNext, R.id.ivPrev})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivPreviousAudioMessages:
                Bundle bundle = new Bundle();
                AudioHistoryFragment audioHistoryFragment = new AudioHistoryFragment();
                audioHistoryFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, audioHistoryFragment).addToBackStack(null).commit();

                break;
            case R.id.ivSettings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsFragment()).addToBackStack(null).commit();
                break;
            case R.id.ivPlay:
                playOrPauseSong();
                break;
            case R.id.ivPrev:

                mBoundService.playPrev();
                myViewPager.setCurrentItem(mBoundService.getIndex());
                ivPlay.setImageResource(R.drawable.pause_icon);
                tvSongTitle.setText(mBoundService.getSongName());
                break;
            case R.id.ivNext:
                mBoundService.playNext();
                myViewPager.setCurrentItem(mBoundService.getIndex());
                ivPlay.setImageResource(R.drawable.pause_icon);
                tvSongTitle.setText(mBoundService.getSongName());
                break;
        }

    }

    public void playOrPauseSong() {
        if (mBoundService != null) {
            mBoundService.setSongList(songsList);
            if (isPlaying) {
                visualizer.setEnabled(false);
                mHandler.removeMessages(SHOW_PROGRESS);
                mBoundService.pauseAudio();
                isPlaying = false;
                ivPlay.setImageResource(R.drawable.play_icon);
            } else {
                isPlaying = true;
                mHandler.sendEmptyMessage(SHOW_PROGRESS);
                mBoundService.playSong(songIndex);
                ivPlay.setImageResource(R.drawable.pause_icon);
                visualizer.setEnabled(true);

            }
        } else {

        }
    }

    void startVisualization(MediaPlayer player) {
        if (player != null) {
            visualizer.setEnabled(true);
            visualizer.setColor(ContextCompat.getColor(MainActivity.this, R.color.colortext));
            visualizer.setPlayer(player);
        } else {
            Toast.makeText(mBoundService, "Player not ready", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onSongSelected(ArrayList<HashMap<String, String>> musicList, int position) {
        getSupportFragmentManager().popBackStack();
        isPlaying = true;
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        songIndex = position;
        ivPrev.setVisibility(View.VISIBLE);
        ivNext.setVisibility(View.VISIBLE);

        if (mBoundService != null) {
            songsList.clear();
            songsList.addAll(musicList);
            mBoundService.setSongList(songsList);
            mBoundService.setIndex(songIndex);
            mBoundService.resetPlayer();
            mBoundService.playSong(songIndex);
            tvSongDuration.setText(Utilities.milliSecondsToTimer(mBoundService.getDuration()));
            tvSongTitle.setText(mBoundService.getSongName());
            ivPlay.setImageResource(R.drawable.pause_icon);
            visualizer.setEnabled(true);
            cardsPagerAdapter.notifyDataSetChanged();
            myViewPager.setCurrentItem(position);
        }

    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                // ...

                case SHOW_PROGRESS:
                    r.run();
                    break;

            }
        }
    };

    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (mBoundService != null && mBoundService.isMediaPlaying()) {
                tvStartTime.setText(Utilities.milliSecondsToTimer(mBoundService.getProgress()));
                tvSongDuration.setText(Utilities.milliSecondsToTimer(mBoundService.getDuration()));
                mHandler.postDelayed(r, 1000);
            }
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < myViewPager.getChildCount(); i++) {
            View cardView = myViewPager.getChildAt(i);
            int itemPosition = (Integer) cardView.getTag();
            Log.d("itemposition", "" + itemPosition);
            if (itemPosition == position) {
                cardView.setScaleX(BIG_SCALE - DIFF_SCALE * positionOffset);
                cardView.setScaleY(BIG_SCALE - DIFF_SCALE * positionOffset);
            }
            if (itemPosition == (position + 1)) {
                cardView.setScaleX(SMALL_SCALE + DIFF_SCALE * positionOffset);
                cardView.setScaleY(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        }

    }

    @Override
    public void onPageSelected(int position) {

        if (position < mBoundService.getIndex()) {
            ivPrev.performClick();
        } else if (position > mBoundService.getIndex()) {
            ivNext.performClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
