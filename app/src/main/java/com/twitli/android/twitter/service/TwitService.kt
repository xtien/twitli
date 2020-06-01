/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.data.*
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitRepository
import com.twitli.android.twitter.wiki.WikiPageManager
import org.apache.commons.lang3.math.NumberUtils
import twitter4j.TwitterException
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitService : LifecycleService() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var contentRepository: ContentRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var twitRepository: TwitRepository

    private val init = 10L
    var count = 999L
    private var intervals: List<String>? = null
    private var active = false

    @Inject
    lateinit var wikiPageManager: WikiPageManager

    @Inject
    lateinit var twitManager: TwitManager

    @Inject
    lateinit var chatbot: ChatBot

    private val es = Executors.newScheduledThreadPool(2)
    private var year = 42
    private var currentYear = 0
    private var tweetedHistory = System.currentTimeMillis()

    inner class LocalBinder : Binder() {
        val service: TwitService
            get() = this@TwitService
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            val message = bundle!!.getString("message")
            es.execute {
                try {
                    val text = wikiPageManager!!.getPage(message)
                    twitManager!!.tweet(text)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOGTAG, "TwitService started")

        (applicationContext as MyApplication).appComponent.inject(this)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        currentYear = calendar[Calendar.YEAR]

        es.execute {
            year = userRepository!!.year
            Log.d(LOGTAG, "year = $year")
        }
        userRepository!!.followersCount?.observeForever { followers: Long? ->
            Log.d(LOGTAG, if ("followers count is $followers" != null) java.lang.Long.toString(followers!!) else "null")
            if (followers != null && followers != 0L) {
                year = (followers % currentYear).toInt()
                Log.d(LOGTAG, "doWiki 111 $year")
                doWiki(year)
            }
        }
        settingsRepository!!.isActive?.observeForever { active: Boolean? ->
            if (active != null) {
                if (active) {
                    if (System.currentTimeMillis() - tweetedHistory > 900000L) {
                        Log.d(LOGTAG, "doWiki 121 $year")
                        doWiki(year)
                    }
                }
            }
        }
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        intervals = Arrays.asList(*applicationContext.resources.getStringArray(R.array.tweet_interval))
        settingsRepository!!.isActive?.observeForever { active: Boolean? ->
            Log.d(LOGTAG, "active = " + this.active + " " + active)
            if (active != null) {
                this@TwitService.active = active
            }
        }

        es.scheduleAtFixedRate({
            Log.d(LOGTAG, "long interval = " + getLongInterval(prefs.getInt("tweet_interval", 2)))
            if (count > getLongInterval(prefs.getInt("tweet_interval", 2)) - 0.1) {
                count = 0L
                if (active) {
                    Log.d(LOGTAG, "getUser() called")
                    user
                }
            } else {
                count += 1L
            }
        }, init, 3600L, TimeUnit.SECONDS)
        val filter = IntentFilter("nl.christine.app.message")
        filter.addAction("com.twitli.app.message")
        registerReceiver(receiver, filter)
        es.schedule({ cleanupTwitRepository() }, 60, TimeUnit.SECONDS)
    }

    private fun cleanupTwitRepository() {
        twitRepository!!.cleanUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        Log.d(LOGTAG, "TwitService stopped")
    }

    private fun tweet(content: Content) {
        Log.d(LOGTAG, "tweet " + content.year + " " + content.text)
        twitManager!!.tweet(content)
    }

    private val user: Int
        private get() = try {
            val user = twitManager!!.verifyCredentials()
            if (user != null) {
                userRepository!!.persist(user)
                user.followersCount
            } else {
                0
            }
        } catch (e: TwitterException) {
            e.printStackTrace()
            1600
        }

    private fun doWiki(year: Int) {
        Log.d(LOGTAG, "doWiki 179 $year")
        es.execute {
            tweetedHistory = System.currentTimeMillis()
            val contentStatus = contentRepository!!.getStatus(year)
            when (contentStatus) {
                ContentStatus.AVAILABLE -> {
                    Log.d(LOGTAG, "AVAILABLE")
                    val content = contentRepository!!.getFirst(Integer.toString(year))
                    if (content != null) {
                        contentRepository!!.setDone(content.id)
                        Log.d(LOGTAG, "setDone " + content.text)
                        tweet(content)
                    } else {
                        Log.e(LOGTAG, "no content found $year")
                    }
                }
                ContentStatus.NONE -> {
                    Log.d(LOGTAG, "NONE")
                    try {
                        wikiPageManager!!.getPage(Integer.toString(year))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                ContentStatus.DONE -> Log.d(LOGTAG, "DONE")
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun getLongInterval(position: Int): Long {
        var interval = intervals!![position]
        interval = interval.substring(0, interval.indexOf(" "))
        return if (NumberUtils.isCreatable(interval)) {
            interval.toInt().toLong()
        } else 1L
    }

    companion object {
        private val LOGTAG = TwitService::class.java.simpleName
    }
}
