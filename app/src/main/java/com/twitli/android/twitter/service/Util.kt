package com.twitli.android.twitter.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

class Util {

    companion object {

        fun scheduleJob(context: Context) {

            val serviceComponent = ComponentName(context, TwitJobService::class.java)
            val builder = JobInfo.Builder(0, serviceComponent)
            builder.setMinimumLatency(10 * 1000.toLong()) // wait at least
            builder.setOverrideDeadline(30 * 1000.toLong()) // maximum delay
            val jobScheduler = context.applicationContext
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(builder.build())
        }
    }
}
