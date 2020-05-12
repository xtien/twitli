package com.twitli.android.twitter.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class Util {

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TwitJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(10 * 1000); // wait at least
        builder.setOverrideDeadline(30 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getApplicationContext()
                .getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
