package com.mx.wifichat;

import android.app.Application;

/**
 * Created by MX on 2015/6/14.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
            instance = this;
    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {

        super.onTrimMemory(level);
    }

    public static MyApplication getInstance() {

        return instance;
    }
}
