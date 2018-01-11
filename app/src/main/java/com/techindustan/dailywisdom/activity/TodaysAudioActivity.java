package com.techindustan.dailywisdom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.service.MusicService;
import com.techindustan.dailywisdom.utils.LineBarVisualizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodaysAudioActivity extends AppCompatActivity {

    @BindView(R.id.ivPlay)
    ImageView ivPlay;
    @BindView(R.id.ivPrev)
    ImageView ivPrev;
    @BindView(R.id.ivNext)
    ImageView ivNext;
    Intent startIntent;
    public static LineBarVisualizer lineBarVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_audio);
        ButterKnife.bind(this);

        lineBarVisualizer = findViewById(R.id.visualizer);
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorGrey));
        lineBarVisualizer.setDensity(70);
    }

    @OnClick(R.id.ivPlay)
    public void playRingtone() {
        startIntent = new Intent(TodaysAudioActivity.this, MusicService.class);
        if (!MusicService.IS_SERVICE_RUNNING) {
            startIntent.setAction(Constant.ACTION.STARTFOREGROUND_ACTION);
            MusicService.IS_SERVICE_RUNNING = true;
            ivPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            startIntent.setAction(Constant.ACTION.STOPFOREGROUND_ACTION);
            MusicService.IS_SERVICE_RUNNING = false;
            ivPlay.setImageResource(android.R.drawable.ic_media_play);
        }
        startService(startIntent);


    }


    @OnClick(R.id.ivNext)
    public void onNextClick() {

    }

    @OnClick(R.id.ivPrev)
    public void onPrevClick() {

    }


}
