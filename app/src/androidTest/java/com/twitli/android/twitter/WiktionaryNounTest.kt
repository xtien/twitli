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
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.rule.MyDaggerMockRule
import com.twitli.android.twitter.dagger.TestComponent
import com.twitli.android.twitter.rule.InitPreferencesTestRule
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

class WiktionaryNounTest {

    @Inject
    lateinit var api: WiktionaryApi

    @Inject
    lateinit var twitManager: TwitManager

    private val nounString = "fiets"

    var user: User? = null

    @Rule
    @JvmField
    var prefsRule = InitPreferencesTestRule()

    @Rule
    @JvmField
    var daggerRule: DaggerMockRule<TestComponent> = MyDaggerMockRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    @Throws(TwitterException::class)
    fun setup() {
        activityRule.launchActivity(Intent())
        val appComponent: TestComponent = (activityRule.activity.application as MyApplication).appComponent as TestComponent
        appComponent.inject(this)
        Mockito.`when`(twitManager.verifyCredentials()).thenReturn(user)
        Mockito.verify(twitManager, Mockito.times(1))?.verifyCredentials()
        Mockito.`when`(api.getWikiWord(nounString)).thenReturn(nounString)
    }

    @Test
    fun testGetNoun() {
        val string = api.getWikiWord(nounString)
        Assert.assertNotNull(string)
    }
}
