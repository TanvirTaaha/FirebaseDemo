package com.tangent.firebasedemo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundThreadPoster {
    ExecutorService mExecutorService = Executors.newCachedThreadPool();
    public static BackgroundThreadPoster newInstance() {
        return new BackgroundThreadPoster();
    }
    public void post(Runnable runnable) {
        mExecutorService.execute(runnable);
    }
}
