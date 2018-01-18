package com.techindustan.dailywisdom.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.adapter.AudioHistoryAdapter;
import com.techindustan.dailywisdom.utils.SongsManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AudioHistoryFragment extends Fragment {
    AudioHistoryAdapter mAdapter;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivSettings)
    ImageView ivSettings;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Intent intent;
    Unbinder unbinder;
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_audio_history, container, false);
        unbinder = ButterKnife.bind(this, v);

        Bundle bundle = this.getArguments();

        ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

        SongsManager plm = new SongsManager();
        // get all songs from sdcard
        this.songsList = plm.getPlayList();

        // looping through playlist
        for (int i = 0; i < songsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = songsList.get(i);

            // adding HashList to ArrayList
            songsListData.add(song);

        }

        mAdapter = new AudioHistoryAdapter(getActivity(), songsListData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    @OnClick(R.id.ivSettings)
    public void openSettings() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsFragment()).addToBackStack(null).commit();


    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.ivBack)
    public void performBackOperation() {
        getFragmentManager().popBackStack();
    }
}
