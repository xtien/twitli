/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.bot.wiki.DictionaryRepository
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.type.Noun
import com.twitli.android.twitter.bot.wiki.type.Verb
import com.twitli.android.twitter.bot.wiki.type.Word
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
import com.twitli.android.twitter.rule.InitPreferencesTestRule
import com.twitli.android.twitter.rule.MyDaggerMockLiveRule
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.ui.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import twitter4j.Status
import twitter4j.TwitterException
import twitter4j.User
import javax.inject.Inject

class WiktionaryNounLiveTest {

    private val statusText: String = "The woman owns a red bicycle"
    private val status: Status = Mockito.mock(Status::class.java)

    @Inject
    lateinit var wikBot: WiktionaryBot

    @Inject
    lateinit var twitManager: TwitManager

    @Inject
    lateinit var dictionaryRepository: DictionaryRepository

    private val nounString = "fiets"

    var user: User? = null

    @Rule
    @JvmField
    var daggerRule: DaggerMockRule<ApiLiveTestComponent> = MyDaggerMockLiveRule()

    @Rule
    @JvmField
    var prefsRule = InitPreferencesTestRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    @Throws(TwitterException::class)
    fun setup() {
        activityRule.launchActivity(Intent())
        val appComponent: ApiLiveTestComponent = (activityRule.activity.application as MyApplication).appComponent as ApiLiveTestComponent
        appComponent.inject(this)
        dictionaryRepository.clear()
        Mockito.`when`(status.text).thenReturn(statusText)
        Mockito.`when`(twitManager.verifyCredentials()).thenReturn(user)
        Mockito.verify(twitManager, Mockito.times(1))?.verifyCredentials()
    }

    @Test
    fun testLiveNounTest() {

        val wordStrings = wikBot.getWords(status)
        Assert.assertNotNull(wordStrings)
        Assert.assertEquals(6, wordStrings.size)

        val verbs: ArrayList<Word> = ArrayList()
        val nouns: ArrayList<Word> = ArrayList()
        val adjectives: ArrayList<Word> = ArrayList()
        for (s in wordStrings) {
            for (w in s) {
                if ("noun" == w.getType()) {
                    nouns.add(w)
                }
                if ("verb" == w.getType()) {
                    verbs.add(w)
                }
                if ("adjective" == w.getType()) {
                    adjectives.add(w)
                }
            }
        }

        Assert.assertEquals(6, wordStrings.size)
        Assert.assertEquals(4, nouns.size)
        Assert.assertEquals(3, verbs.size)
        Assert.assertEquals(2, adjectives.size)
    }
}
