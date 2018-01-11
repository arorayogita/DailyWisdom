package com.techindustan.dailywisdom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.service.MusicService;
import com.techindustan.dailywisdom.utils.LineBarVisualizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.ivPreviousAudioMessages)
    ImageView ivPreviousAudioMessages;
    @BindView(R.id.ivSettings)
    ImageView ivSettings;

    Intent intent;
    @BindView(R.id.tvDayTime)
    TextView tvDayTime;
    Intent startIntent;
    public static LineBarVisualizer lineBarVisualizer;
    @BindView(R.id.ivPlay)
    ImageView ivPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        lineBarVisualizer = findViewById(R.id.visualizer);
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorGrey));
        lineBarVisualizer.setDensity(70);
    }


    @OnClick(R.id.ivPreviousAudioMessages)
    public void openAudioHistoryMessages() {
        intent = new Intent(MainActivity.this, AudioHistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ivSettings)
    public void openSettings() {
        intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.tvDayTime)
    public void openDayTimeScreen() {
        intent = new Intent(MainActivity.this, DayTimeActivity.class);
        startActivity(intent);


    }

    @OnClick(R.id.ivPlay)
    public void playRingtone() {
        startIntent = new Intent(MainActivity.this, MusicService.class);
        if (!MusicService.IS_SERVICE_RUNNING) {
            startIntent.setAction(Constant.ACTION.STARTFOREGROUND_ACTION);
            MusicService.IS_SERVICE_RUNNING = true;
            ivPlay.setImageResource(R.drawable.pause_icon);
        } else {
            startIntent.setAction(Constant.ACTION.STOPFOREGROUND_ACTION);
            MusicService.IS_SERVICE_RUNNING = false;
            ivPlay.setImageResource(R.drawable.play_icon);
        }
        startService(startIntent);


    }

}
