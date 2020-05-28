/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.tweet.impl

import android.content.Context
import android.util.Log
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.data.Content
import com.twitli.android.twitter.tweet.TwitManager
import twitter4j.*
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import java.util.concurrent.Executors
import javax.inject.Inject

class TwitManagerImpl : TwitManager {

    private val twitter: Twitter

    var context: Context = MyApplication.instance

    @Inject
    lateinit var chatbot : ChatBot

    var es = Executors.newCachedThreadPool()!!

    init {
        (context!!.applicationContext as MyApplication).appComponent!!.inject(this)
    }

    constructor() {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val accessTokenKey = prefs.getString("access_token", null)
        val accesTokenSecret = prefs.getString("access_token_secret", null)
        twitter = TwitterFactory.getSingleton()
        twitter.setOAuthConsumer(context.getString(R.string.api_key), context.getString(R.string.api_secret))
        if (accessTokenKey != null && accesTokenSecret != null) {
            val accessToken = AccessToken(accessTokenKey, accesTokenSecret)
            twitter.oAuthAccessToken = accessToken
        }
    }

    override fun tweet(content: Content?) {
        val string: String = content?.year.toString() + ", " + (if (content?.date != null) content.date.toString() + ". " else "") + content?.text
        tweet(string)
    }

    override fun reply(string: String?, replyToId: Long?) {
        tweetIt(string, replyToId)
    }

    override fun tweet(string: String?) {
        tweetIt(string, null)
    }

    private fun tweetIt(string: String?, replyToId: Long?) {
        es.execute {
            try {
                var twitString = (if (string!!.length > 280) string.substring(0, 280) else string)
                if (twitString.length > 280) {
                    twitString = twitString.substring(0, twitString.lastIndexOf(" "))
                    if (twitString.length < 275) {
                        twitString += " ..."
                    }
                }
                val statusUpdate = StatusUpdate(twitString)
                if (replyToId != null) {
                    statusUpdate.inReplyToStatusId = replyToId
                }
                twitter.updateStatus(statusUpdate)
                Log.d(LOGTAG, "status $twitString")
            } catch (e: TwitterException) {
                val statusCode = e.statusCode
                if (statusCode == 403) {
                    val message = e.errorMessage
                    Log.e(LOGTAG, "statusCode $statusCode $message")
                } else {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun createAccessToken(accessTokenVerifier: String?) {
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        try {
            val requestToken = RequestToken(prefs.getString("request_token", null), prefs.getString("request_token_secret", null))
            if (requestToken != null) {
                val accessToken = twitter.getOAuthAccessToken(requestToken, accessTokenVerifier)
                val editor = prefs.edit()
                editor.putString("access_token_secret", accessToken.tokenSecret)
                editor.putString("access_token", accessToken.token)
                editor.putLong("user_id", accessToken.userId)
                editor.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(TwitterException::class)
    override fun createRequestToken(): RequestToken? {
        val requestToken = twitter.oAuthRequestToken
        val editor = context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
        editor.putString("request_token", requestToken.token)
        editor.putString("request_token_secret", requestToken.tokenSecret)
        editor.apply()
        return requestToken
    }

    @Throws(TwitterException::class)
    override fun verifyCredentials(): User? {
        return twitter.verifyCredentials()
    }

    override fun like(tweetId: Long?) {
        try {
            twitter.createFavorite(tweetId!!)
        } catch (e: TwitterException) {
            e.printStackTrace()
        }
    }

    override fun unlike(tweetId: Long?) {
        try {
            twitter.destroyFavorite(tweetId!!)
        } catch (e: TwitterException) {
            e.printStackTrace()
        }
    }

    @Throws(TwitterException::class)
    override fun getHomeTimeline(paging: Paging?): List<Status?>? {
        var tweets = twitter.getHomeTimeline(paging)
        chatbot.read(tweets)
        return tweets
    }

    override fun logout() {
        val editor = context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
        editor.remove("request_token")
        editor.remove("request_token_secret")
        editor.apply()
    }

    companion object {
        private val LOGTAG = TwitManagerImpl::class.java.simpleName
    }
}
