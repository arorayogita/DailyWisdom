package com.techindustan.dailywisdom.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.adapter.AudioHistoryAdapter;

import java.util.ArrayList;

public class AudioHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AudioHistoryAdapter mAdapter;
    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_history);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        names.add("title");
        names.add("title");
        mAdapter = new AudioHistoryAdapter(AudioHistoryActivity.this, names);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
