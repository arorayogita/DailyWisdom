package com.techindustan.dailywisdom.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.adapter.CarouselPagerAdapter;
import com.techindustan.dailywisdom.fragment.AudioHistoryFragment;
import com.techindustan.dailywisdom.fragment.SettingsFragment;
import com.techindustan.dailywisdom.service.MusicService;
import com.techindustan.dailywisdom.utils.Constant;
import com.techindustan.dailywisdom.utils.LineBarVisualizer;
import com.techindustan.dailywisdom.utils.Utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.ivPreviousAudioMessages)
    ImageView ivPreviousAudioMessages;
    @BindView(R.id.ivSettings)
    ImageView ivSettings;
    Intent intent;

    Intent startIntent;
    public static LineBarVisualizer lineBarVisualizer;
    public TextView tvSongDuration;

    public static ImageView ivPlay;
    @BindView(R.id.ivaudio)
    ImageView ivaudio;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    @BindView(R.id.ivPrev)
    ImageView ivPrev;
    @BindView(R.id.ivNext)
    ImageView ivNext;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    private String downloadAudioPath;
    private String urlDownloadLink = "";

    private ProgressBar progressbar;
    String filename;
    List<String> musicList;
    String type;
    public ViewPager pager;
    public CarouselPagerAdapter adapter;
    public static int count = 3; //ViewPager items size
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 3;
    public final static int LOOPS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        lineBarVisualizer = findViewById(R.id.visualizer);
        pager = (ViewPager) findViewById(R.id.myviewpager);

        tvSongDuration = findViewById(R.id.tvSongDuration);
        ivPlay = findViewById(R.id.ivPlay);
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colortext));
        lineBarVisualizer.setDensity(70);
        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "voices");
        if (!audioVoice.exists()) {
            audioVoice.mkdir();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("multiplesongs");

            if (type.matches("multiplesongs")) {
                ivPrev.setVisibility(View.VISIBLE);
                ivNext.setVisibility(View.VISIBLE);
                ivaudio.setVisibility(View.GONE);
                pager.setVisibility(View.VISIBLE);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int pageMargin = ((metrics.widthPixels / 4) * 2);
                pager.setPageMargin(-pageMargin);
                adapter = new CarouselPagerAdapter(this, this.getSupportFragmentManager());
                pager.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pager.addOnPageChangeListener(adapter);
                pager.setCurrentItem(FIRST_PAGE);
                pager.setOffscreenPageLimit(3);
            }

        }
        Log.e("downloadAudioPath", "" + downloadAudioPath);

        progressbar = (ProgressBar) findViewById(R.id.progress_view);

        urlDownloadLink = "https://mp3oye.com/stream/20bbc370c9872271f2b1df1350599bee";
        filename = extractFilename();
        MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.red_e);
        tvSongDuration.setText(Utilities.milliSecondsToTimer(player.getDuration()));
/*
        downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
        DownloadFile downloadAudioFile = new DownloadFile();

        downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);
*/

    }


    @OnClick(R.id.ivPreviousAudioMessages)
    public void openAudioHistoryMessages() {
        //  readFromFile();
        Bundle bundle = new Bundle();
        AudioHistoryFragment audioHistoryFragment = new AudioHistoryFragment();
        bundle.putStringArrayList("musicList", (ArrayList<String>) musicList);
        audioHistoryFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, audioHistoryFragment).addToBackStack(null).commit();
      /*  Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
        startActivity(intent);*/

    }

    @OnClick(R.id.ivSettings)
    public void openSettings() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsFragment()).addToBackStack(null).commit();


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


    private class DownloadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... url) {
            int count;
            try {
                URL urls = new URL(url[0]);
                URLConnection connection = urls.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(urls.openStream());
                OutputStream output = new FileOutputStream(url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                Toast.makeText(MainActivity.this, "downloaded", Toast.LENGTH_SHORT).show();

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(ProgressBar.VISIBLE);
            Toast.makeText(MainActivity.this, "started", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(ProgressBar.GONE);
            Toast.makeText(MainActivity.this, "finished", Toast.LENGTH_SHORT).show();
        }
    }

    private String extractFilename() {
        if (urlDownloadLink.equals("")) {
            return "";
        }
        String newFilename = "";
        if (urlDownloadLink.contains("/")) {
            int dotPosition = urlDownloadLink.lastIndexOf("/");
            newFilename = urlDownloadLink.substring(dotPosition + 1, urlDownloadLink.length());
            newFilename = newFilename + ".mp3";
        } else {
            newFilename = urlDownloadLink;
        }
        return newFilename;
    }

    public String convertDuration(long duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }

 /*   public void getSongDuration() {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource();

        String out = "";
        // get mp3 info

        // convert duration to minute:seconds
        String duration =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.v("time", duration);
        long dur = Long.parseLong(duration);
        String seconds = String.valueOf((dur % 60000) / 1000);

        Log.v("seconds", seconds);
        String minutes = String.valueOf(dur / 60000);
        out = minutes + ":" + seconds;
        if (seconds.length() == 1) {
            tvSongDuration.setText("0" + minutes + ":0" + seconds);
        } else {
            tvSongDuration.setText("0" + minutes + ":" + seconds);
        }
        Log.v("minutes", minutes);
        // close object
        metaRetriever.release();
    }*/

    private List<String> readFromFile() {
        musicList = new ArrayList<>();

        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/voices/");
// lists all the files into an array
        File[] dirFiles = fileDirectory.listFiles();

        if (dirFiles.length != 0) {
            // loops through the array of files, outputing the name to console
            for (int ii = 0; ii < dirFiles.length; ii++) {
                String fileOutput = dirFiles[ii].toString();
                System.out.println(fileOutput);
                String newFilename = "";
                if (fileOutput.contains("/")) {
                    int dotPosition = fileOutput.lastIndexOf("/");
                    newFilename = fileOutput.substring(dotPosition + 1, fileOutput.length());
                } else {
                    newFilename = fileOutput;
                }
                musicList.add(newFilename);
                Log.e("file", "" + newFilename);

            }
        }
        return musicList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colortext));
        lineBarVisualizer.setDensity(70);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
