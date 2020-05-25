/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.dagger.MyDaggerMockRule
import com.twitli.android.twitter.dagger.TestComponent
import com.twitli.android.twitter.ui.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import twitter4j.TwitterException
import javax.inject.Inject

class WiktionaryNounTest {
    @Inject
    var api: WiktionaryApi? = null
    private val nounString = "fiets"

    @Rule
    var daggerRule: DaggerMockRule<TestComponent> = MyDaggerMockRule()

    @Rule
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    @Throws(TwitterException::class)
    fun setup() {
        activityRule.launchActivity(Intent())
        val appComponent: TestComponent = (activityRule.activity.application as MyApplication).appComponent as TestComponent
        appComponent.inject(this)
    }

    @Test
    fun testGetNoun() {
        val string = api!!.getWikiWord(nounString)
        Assert.assertNotNull(string)
    }
}
