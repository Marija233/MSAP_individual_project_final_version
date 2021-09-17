package com.example.msap_individual_project_final_version.sync;


import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class EarthquakeSyncUtils {

    private static final String LOG_TAG = EarthquakeSyncUtils.class.getSimpleName();
    private static final int SYNC_INTERVAL_PERIODICITY = (int) TimeUnit.HOURS.toSeconds(24);
    private static final int SYNC_INTERVAL_TOLERANCE = (int) TimeUnit.HOURS.toSeconds(1);

    private static boolean sInitialized;

    private static final String EARTHQUAKE_SYNC_TAG = "earthquake-sync";

    static void scheduleFirebaseJobDispatcherSync(final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncEarthquakeJob = dispatcher.newJobBuilder()
                .setService(EarthquakeFirebaseJobService.class)
                .setTag(EARTHQUAKE_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        (60*60*24),
                        (60*60*24) + 60))
                .setReplaceCurrent(true)
                .build();

        Timber.e("Notification Trigger.executionWindow(\n" +
                "                        //SYNC_INTERVAL_PERIODICITY,\n" +
                "                        (60*60*24),\n" +
                "                        //SYNC_INTERVAL_PERIODICITY + SYNC_INTERVAL_TOLERANCE))\n" +
                "                        (60*60*24) + 60)): ");

        dispatcher.schedule(syncEarthquakeJob);
    }

    synchronized public static void initialize(final Context context) {

        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);
    }

}