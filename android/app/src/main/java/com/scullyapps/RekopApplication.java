package com.scullyapps;

import android.app.Application;
import android.content.res.Configuration;

public class RekopApplication extends Application {

    private static RekopApplication application;

    public static RekopApplication getInstance() {
        return application;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
