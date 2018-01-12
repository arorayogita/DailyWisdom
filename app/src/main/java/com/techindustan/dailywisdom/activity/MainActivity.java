package com.techindustan.dailywisdom.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.service.MusicService;
import com.techindustan.dailywisdom.utils.LineBarVisualizer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.Telephony.Mms.Part.FILENAME;

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

    public static ImageView ivPlay;
    private String downloadAudioPath;
    private String urlDownloadLink = "";

    private ProgressBar progressbar;
    String filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        lineBarVisualizer = findViewById(R.id.visualizer);
        ivPlay = findViewById(R.id.ivPlay);
        lineBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorGrey));
        lineBarVisualizer.setDensity(70);
        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "voices");
        if (!audioVoice.exists()) {
            audioVoice.mkdir();
        }

        Log.e("downloadAudioPath", "" + downloadAudioPath);

        progressbar = (ProgressBar) findViewById(R.id.progress_view);

        urlDownloadLink = "https://files1.mp3oye.com/stream/7403911c09502ad5fefdd1dc523de7a5";

        String[] items = {"https://mp3oye.com/stream/4376742b719bf9a660a56e3ba8e7c360", "https://files1.mp3oye.com/stream/7403911c09502ad5fefdd1dc523de7a5", "https://mp3oye.com/stream/20bbc370c9872271f2b1df1350599bee", "https://files1.mp3oye.com/stream/951c71e91dba03dec0387770822b2979", "https://mp3oye.com/stream/373f5e056e1c31a02d6b4f3eba53bdf7"};

    }


    @OnClick(R.id.ivPreviousAudioMessages)
    public void openAudioHistoryMessages() {
        intent = new Intent(MainActivity.this, AudioHistoryActivity.class);
        startActivity(intent);
        filename = extractFilename();
        Toast.makeText(this, "filename" + filename, Toast.LENGTH_SHORT).show();
        downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;

        DownloadFile downloadAudioFile = new DownloadFile();
        downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);
        // readFromFile();
    }

    @OnClick(R.id.ivSettings)
    public void openSettings() {
        intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        readFromFile();
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

    /*  public void getSongDuration(){
          MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
          metaRetriever.setDataSource(filePath);

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
              txtTime.setText("0" + minutes + ":0" + seconds);
          }else {
              txtTime.setText("0" + minutes + ":" + seconds);
          }
          Log.v("minutes", minutes);
          // close object
          metaRetriever.release();
      }*/
    private String readFromFile() {
        List<String> musicList = new ArrayList<>();

        File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/voices/");
// lists all the files into an array
        File[] dirFiles = fileDirectory.listFiles();

        if (dirFiles.length != 0) {
            // loops through the array of files, outputing the name to console
            for (int ii = 0; ii < dirFiles.length; ii++) {
                String fileOutput = dirFiles[ii].toString();
                System.out.println(fileOutput);
                musicList.add(fileOutput);
                Log.e("file", "" + musicList);

            }
        }
        return null;
    }
}
