package com.techindustan.dailywisdom.utils;

import android.app.Application;

import com.techindustan.dailywisdom.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by android on 16/1/18.
 */

public class AppController extends Application {


    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
