package com.techindustan.dailywisdom.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techindustan.dailywisdom.activity.AudioHistoryActivity;
import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.AudioListActivity;

import java.util.ArrayList;

/**
 * Created by android on 9/1/18.
 */

public class AudioHistoryAdapter extends RecyclerView.Adapter {
    Activity context;
    ArrayList<String> names;

    public AudioHistoryAdapter(AudioHistoryActivity activity, ArrayList<String> names) {
        this.context = activity;
        this.names = names;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_items, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.title.setText("Title");
        holder1.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AudioListActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

        }
    }
}