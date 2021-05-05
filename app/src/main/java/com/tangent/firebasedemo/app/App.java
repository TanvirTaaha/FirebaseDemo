package com.tangent.firebasedemo.app;

import android.app.Application;
import android.content.Context;

import com.tangent.firebasedemo.BuildConfig;
import com.tangent.firebasedemo.utils.TagTree;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class App extends Application {
    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<>(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new TagTree("Taaha", true));
        }
    }

    public static Context getAppContext() {
        return mContext.get();
    }
}
