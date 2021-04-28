package com.tangent.firebasedemo.app;

import android.app.Application;

import com.tangent.firebasedemo.BuildConfig;
import com.tangent.firebasedemo.utils.TagTree;

import timber.log.Timber;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new TagTree("Taaha", true));
        }
    }
}
