/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.ui

import android.content.*
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

    private val AUTH: Int = 2
    private val INTRO: Int = 1
    lateinit var twitService: TwitService
    private val es = Executors.newCachedThreadPool()
    private val tweetLoadTime = 120000L
    lateinit var requestToken: RequestToken

    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Inject
    lateinit var chatBot: ChatBot

    @Inject
    lateinit var twitRepository: TwitRepository

    @Inject
    lateinit var twitManager: TwitManager

    private var pagerAdapter: ScreenSlidePagerAdapter? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            INTRO -> doActivity()
            AUTH -> {
                twitManager.reset()
                doActivity()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MyApplication).appComponent.inject(this)
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        editor = prefs.edit()

        if (prefs.getBoolean("authorizing", false)) {
            doActivity()
        } else {
            if (prefs.getInt("inits", 0) < 2 && (prefs.getLong("previousShowIntro", 0L) < (System.currentTimeMillis() - 3600000))) {
                startActivityForResult(Intent(this, IntroActivity::class.java), INTRO)
                editor.putInt("inits", prefs.getInt("inits", 0) + 1)
                editor.putLong("previousShowIntro", System.currentTimeMillis());
                editor.apply()
            } else {
                doActivity()
            }
        }
    }

    private fun doActivity() {
        setContentView(R.layout.activity_main)
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(TwitFragment.newInstance())
        pagerAdapter!!.addFragment(SettingsFragment.newInstance())
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter = pagerAdapter
        val icycle = Intent(this, TwitService::class.java)
        bindService(icycle, connection, Context.BIND_AUTO_CREATE)

        es.execute {
            var accessToken = prefs.getString("access_token", null)
            var accesTokenSecret = prefs.getString("access_token_secret", null)
            val accessTokenVerifier = prefs.getString("access_token_verifier", null)

            if (accessToken != null && accesTokenSecret != null) {
                try {
                    twitManager.verifyCredentials()
                    if (prefs.getLong("last_tweets_loaded", 0L) < System.currentTimeMillis() - tweetLoadTime) {
                        editor.putLong("last_tweets_loaded", System.currentTimeMillis())
                        editor.apply()
                        twitRepository.loadTweets()
                    }
                } catch (e: TwitterException) {
                    if (e.statusCode == TwitterException.UNAUTHORIZED) {
                        editor.putString("access_token", null)
                        editor.putString("access_token_verifier", null)
                        editor.putString("access_token_secret", null)
                        editor.apply()
                    }
                }
            }
            if (accessTokenVerifier != null && accesTokenSecret == null) {
                twitManager.createAccessToken(accessTokenVerifier)
            } else if (accessToken == null) {
                authorize()
            }
        }
    }

    private fun authorize() {

        es.submit {
            try {
                requestToken = twitManager.createRequestToken()
            } catch (e: TwitterException) {
                e.printStackTrace()
            }
            editor.putBoolean("authorizing", true);
            editor.apply()
            runOnUiThread {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.authorizationURL))
                startActivityForResult(browserIntent, AUTH)
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
        private val LOGTAG: String = MainActivity::class.java.simpleName
    }
}
