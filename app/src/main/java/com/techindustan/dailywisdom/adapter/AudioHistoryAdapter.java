package com.techindustan.dailywisdom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by android on 9/1/18.
 */

public class AudioHistoryAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<HashMap<String, String>> musicList;

    public AudioHistoryAdapter(FragmentActivity activity, ArrayList<HashMap<String, String>> musicList) {
        this.context = activity;
        this.musicList = musicList;
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
        holder1.title.setText(musicList.get(position).get("songTitle"));
        final int songIndex = position;
        holder1.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("multiplesongs", "multiplesongs");
                intent.putExtra("songIndex", songIndex);
                intent.putExtra("songlist",musicList);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        LinearLayout llMain;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            llMain = (LinearLayout) view.findViewById(R.id.llMain);

        }
    }

}