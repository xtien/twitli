package com.twitli.android.twitter.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

public class TwitJobService extends JobService {

    private static final String LOGTAG = TwitJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(LOGTAG, "JobService started");
        Intent service = new Intent(getApplicationContext(), TwitService.class);
        getApplicationContext().startService(service);
        Util.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(LOGTAG, "JobService stopped");
        return true;
    }
}
