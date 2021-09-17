package com.example.msap_individual_project_final_version;

import android.app.Application;
import androidx.multidex.BuildConfig;
import timber.log.Timber;

public class EarthquakeApp extends Application {
    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
