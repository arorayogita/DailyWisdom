package com.techindustan.dailywisdom.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techindustan.dailywisdom.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by binod on 19/1/18.
 */

public class AudioBackgroundAdapter extends PagerAdapter {
    private boolean mIsDefaultItemSelected = false;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    ArrayList<HashMap<String, String>> songsList;
    Context context;
    int j = 0;
    public AudioBackgroundAdapter(ArrayList<HashMap<String, String>> songlist, Context context) {
        this.songsList = songlist;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView cardImageView = (ImageView) View.inflate(container.getContext(), R.layout.imageview_card, null);
        j++;
        switch (j) {
            case 1:
                cardImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.audio_icon));
                break;
            case 2:
                cardImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.audio_prev));
                break;
            case 3:
                cardImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.audio_next));
                j = 0;
                break;
        }


        Log.e("position", "" + position);
        cardImageView.setTag(position);
        if (!mIsDefaultItemSelected) {
            cardImageView.setScaleX(BIG_SCALE);
            cardImageView.setScaleY(BIG_SCALE);
            mIsDefaultItemSelected = true;
        } else {
            cardImageView.setScaleX(SMALL_SCALE);
            cardImageView.setScaleY(SMALL_SCALE);
        }
        container.addView(cardImageView);
        return cardImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}