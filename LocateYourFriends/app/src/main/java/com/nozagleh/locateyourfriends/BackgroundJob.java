package com.nozagleh.locateyourfriends;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.util.Log;

/**
 * Created by arnarfreyr on 3.12.2017.
 */

@TargetApi(21)
public class BackgroundJob extends JobService {
    private static final String TAG = "BackgroundJob";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "starting service");
        GPSManager.startLocationTracking(getApplicationContext());

        APIConnector.addUserLocation(
                LocalDataManager.getUserId(getApplicationContext()),
                GPSManager.getLocation(getApplicationContext())
        );

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        GPSManager.stopLocationTracking();
        return true;
    }
}

@TargetApi(23)
class BackgroundManager {
    private static final String TAG = "BackgroundManager";

    private static final int JOB_ID_SEND_LOCATION = 12;

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, BackgroundJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID_SEND_LOCATION, serviceComponent);
        builder.setPeriodic(5000)
                .setPersisted(true);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
}

