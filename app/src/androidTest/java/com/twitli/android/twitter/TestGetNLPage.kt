/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
import com.twitli.android.twitter.data.ContentRepository
import com.twitli.android.twitter.rule.InitPreferencesTestRule
import com.twitli.android.twitter.rule.MyDaggerMockLiveRule
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.ui.MainActivity
import com.twitli.android.twitter.wiki.WikiPageManager
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import twitter4j.TwitterException
import twitter4j.User
import java.util.*
import javax.inject.Inject


class TestGetNLPage {

    @Inject
    lateinit var contentRepository: ContentRepository

    @Inject
    lateinit var wikiPageManager: WikiPageManager

    @Inject
    lateinit var twitManager: TwitManager

    var user: User? = null

    @Rule
    @JvmField
    var daggerRule: DaggerMockRule<ApiLiveTestComponent> = MyDaggerMockLiveRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Rule
    @JvmField
    var prefsRule = InitPreferencesTestRule()

    @Before
    @Throws(TwitterException::class)
    fun setup() {

        activityRule.launchActivity(Intent())
        setLocale(Locale("nl", "NL"))
        val appComponent: ApiLiveTestComponent = (activityRule.activity.application as MyApplication).appComponent as ApiLiveTestComponent
        appComponent.inject(this)
        contentRepository.clear();

        Mockito.`when`(twitManager!!.verifyCredentials()).thenReturn(user)
        Mockito.verify(twitManager!!, Mockito.times(1))?.verifyCredentials()
    }

    @Test
    fun getPageNL() {
        wikiPageManager.getPage("1")
        var content = contentRepository!!.getFirst("1")
        val list = contentRepository!!.getAll("1")
        Assert.assertNotNull(list)
        Assert.assertEquals(10, list?.size)
        Assert.assertNotNull(content)
        Assert.assertTrue(content!!.text!!.contains("Aarde"))
        contentRepository!!.setDone(content.id)
        content = contentRepository!!.getFirstUnused("1")
        Assert.assertNotNull(content)
        Assert.assertTrue(content!!.text!!.contains("China"))
    }

    private fun setLocale(locale: Locale?) {
        val resources: Resources = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext.resources
        Locale.setDefault(locale)
        val config: Configuration = resources.configuration
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
