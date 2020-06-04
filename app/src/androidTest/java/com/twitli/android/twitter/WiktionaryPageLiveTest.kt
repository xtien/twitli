/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Context
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.type.Word
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
import com.twitli.android.twitter.rule.MyDaggerMockLiveRule
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.ui.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import twitter4j.TwitterException
import twitter4j.User
import javax.inject.Inject

class WiktionaryPageLiveTest {

    @Inject
    lateinit var bot: WiktionaryBot

    @Inject
    lateinit var twitManager: TwitManager

    private val nounString = "bicycle"

    var user: User? = null

    @Rule
    @JvmField
    var daggerRule: DaggerMockRule<ApiLiveTestComponent> = MyDaggerMockLiveRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    @Throws(TwitterException::class)
    fun setup() {
        val prefs = InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString("access_token", "123")
        edit.putString("access_token_secret", "123")
        edit.putLong("last_tweets_loaded", System.currentTimeMillis())
        edit.apply()
        activityRule.launchActivity(Intent())
        val appComponent: ApiLiveTestComponent = (activityRule.activity.application as MyApplication).appComponent as ApiLiveTestComponent
        appComponent.inject(this)
        Mockito.`when`(twitManager!!.verifyCredentials()).thenReturn(user)
        Mockito.verify(twitManager!!, Mockito.times(1))?.verifyCredentials()
    }

    @Test
    fun testLiveGetNoun() {
        val word :List<Word> = bot.getType(nounString)
        Assert.assertNotNull(word)
        Assert.assertTrue(nounString == word[0].getWordString())
        Assert.assertTrue("noun" == word[0].getType())
    }
}
