/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.tweet

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.twitli.android.twitter.data.AppDatabase
import twitter4j.Paging
import twitter4j.Status
import twitter4j.TwitterException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitRepository @Inject constructor(application: Application?, twitManager: TwitManager) {

    private val twitManager: TwitManager
    private val twitDao: TwitDao
    val tweets: LiveData<List<Tweet?>?>?
    var es = Executors.newScheduledThreadPool(1)!!

    fun create(tweet: Tweet?) {
        AppDatabase.databaseWriteExecutor.execute { twitDao.create(tweet) }
    }

    fun loadTweets() {
        val paging = Paging()
        paging.count = 20
        var tweets: List<Status?>? = ArrayList()
        try {
            tweets = twitManager.getHomeTimeline(paging)
         } catch (e: TwitterException) {
            Log.e(LOGTAG, "Twitter error " + e.exceptionCode + " " + e.message)
        }
        for (status in tweets!!) {
            twitDao.store(status!!.id, status.user.name, status.user.screenName, status.createdAt.time, status.text, status.isFavorited, status.favoriteCount)
        }
    }

    fun clear() {
        twitDao.clear()
    }

    fun getTweet(id: Int): LiveData<Tweet?>? {
        return twitDao.getTweet(id)
    }

    fun cleanUp() {
        twitDao.cleanUp(System.currentTimeMillis() - ONEHOUR)
    }

    fun getNewTweet(): Tweet {
        return twitDao.getNewTweet()
    }

    fun setTweetDone(tweetId: Long) {
        twitDao.setTweetDone(tweetId)
    }

    companion object {
        private val LOGTAG = TwitRepository::class.java.simpleName
        private const val ONEHOUR = 3600000L
    }

    init {
        val db: AppDatabase? = application?.let { AppDatabase.getDatabase(it) }
        twitDao = db?.twitDao()!!
        tweets = twitDao.tweets
        this.twitManager = twitManager
        es.scheduleAtFixedRate({ loadTweets() }, 300, 300, TimeUnit.SECONDS)
    }
}
