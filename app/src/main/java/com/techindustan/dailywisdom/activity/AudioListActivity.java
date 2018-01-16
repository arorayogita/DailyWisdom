package com.techindustan.dailywisdom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.adapter.CarouselPagerAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AudioListActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_audio_list);

       /* pager = (ViewPager) findViewById(R.id.myviewpager);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 4) * 2);
        pager.setPageMargin(-pageMargin);
        adapter = new CarouselPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pager.addOnPageChangeListener(adapter);
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(3);*/


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
