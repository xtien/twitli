/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.service.TwitService
import com.twitli.android.twitter.service.TwitService.LocalBinder
import com.twitli.android.twitter.tweet.TwitFragment
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitRepository
import twitter4j.TwitterException
import twitter4j.auth.RequestToken
import java.util.concurrent.Executors
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var twitService: TwitService? = null
    private val es = Executors.newCachedThreadPool()
    private val tweetLoadTime = 120000L
    var requestToken: RequestToken? = null

    @Inject
    lateinit var twitManager: TwitManager

    @Inject
    lateinit var chatbot: ChatBot

    private var pagerAdapter: ScreenSlidePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(TwitFragment.newInstance())
        pagerAdapter!!.addFragment(SettingsFragment.newInstance())
        val viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager.setAdapter(pagerAdapter)
        (applicationContext as MyApplication).appComponent?.inject(this)
        val icycle = Intent(this, TwitService::class.java)
        bindService(icycle, connection, Context.BIND_AUTO_CREATE)

        es.execute {
            var accessToken = prefs.getString("access_token", null)
            val accessTokenVerifier = prefs.getString("access_token_verifier", null)
            var accesTokenSecret = prefs.getString("access_token_secret", null)
            if (accessToken != null && accesTokenSecret != null) {
                try {
                    twitManager!!.verifyCredentials()
                    if (prefs.getLong("last_tweets_loaded", 0L) < System.currentTimeMillis() - tweetLoadTime) {
                        val editor = prefs.edit()
                        editor.putLong("last_tweet_load", System.currentTimeMillis())
                        editor.apply()
                        TwitRepository(application, twitManager, chatbot).loadTweets()
                    }
                } catch (e: TwitterException) {
                    if (e.statusCode == TwitterException.UNAUTHORIZED) {
                        accessToken = null
                        accesTokenSecret = null
                        val editor = prefs.edit()
                        editor.putString("access_token", null)
                        editor.putString("access_token_verifier", null)
                        editor.putString("access_token_secret", null)
                        editor.apply()
                    }
                }
            }
            if (accessTokenVerifier != null && accesTokenSecret == null) {
                twitManager!!.createAccessToken(accessTokenVerifier)
            } else if (accessToken == null) {
                try {
                    requestToken = twitManager!!.createRequestToken()
                } catch (e: TwitterException) {
                    e.printStackTrace()
                }
                runOnUiThread {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(requestToken!!.authorizationURL))
                    startActivity(browserIntent)
                    finish()
                }
            }
        }
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            val binder = service as LocalBinder
            twitService = binder.service
        }

        override fun onServiceDisconnected(arg0: ComponentName) {}
    }

    companion object {
        private val LOGTAG: String = MainActivity::class.java.getSimpleName()
    }
}
