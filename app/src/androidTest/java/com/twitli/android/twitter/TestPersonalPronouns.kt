/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.bot.dict.DictionaryRepository
import com.twitli.android.twitter.bot.dict.type.Gender
import com.twitli.android.twitter.bot.dict.type.Noun
import com.twitli.android.twitter.bot.dict.type.PersonalPronoun
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
import com.twitli.android.twitter.dagger.TestComponent
import com.twitli.android.twitter.rule.InitPreferencesTestRule
import com.twitli.android.twitter.rule.MyDaggerMockLiveRule
import com.twitli.android.twitter.ui.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import twitter4j.TwitterException
import javax.inject.Inject

class TestPersonalPronouns {

    @Inject
    lateinit var bot: WiktionaryBot

    @Inject
    lateinit var dictionaryRepository: DictionaryRepository

    @Rule
    @JvmField
    var prefsRule = InitPreferencesTestRule()

    @Rule
    @JvmField
    var daggerRule: DaggerMockRule<ApiLiveTestComponent> = MyDaggerMockLiveRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    @Throws(TwitterException::class)
    fun setup() {
        activityRule.launchActivity(Intent())
        val appComponent: TestComponent = (activityRule.activity.application as MyApplication).appComponent as TestComponent
        appComponent.inject(this)
        activityRule.activity.application.deleteDatabase("dict_database")
    }

    @Test
    fun testPersonalPronounsPopulate() {

        var noun = Noun()
        noun.singular = "car"
        noun.plural = "cars"
        noun.wordString = "car"
        noun.type = "noun"
        dictionaryRepository.create(noun)

        var result = dictionaryRepository.getNouns()

        var pp = PersonalPronoun()
        pp.setGender(Gender.F)
        pp.type ="personalpronoun"
        pp.wordString = "me"
        pp.setPerson(1)
        pp.setPlural(false)
        dictionaryRepository.create(pp)
        var resultPP = dictionaryRepository.getPersonalPronouns()
        Assert.assertEquals(29, resultPP.size)

        var me = bot.getType("me")
        Assert.assertTrue(me.isNotEmpty())
    }
}
