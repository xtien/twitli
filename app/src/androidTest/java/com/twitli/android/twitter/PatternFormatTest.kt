/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.dict.Patterns
import com.twitli.android.twitter.bot.dict.type.*
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
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

class PatternFormatTest {

    @Inject
    lateinit var chatbot: ChatBot

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
        val appComponent: ApiLiveTestComponent = (activityRule.activity.application as MyApplication).appComponent as ApiLiveTestComponent
        appComponent.inject(this)
    }

    @Test
    fun testPattern1() {

        val words = arrayOf("red", "bike")

        val pattern = Patterns.patterns[2]
        val resultString = String.format("What about the %s %s?", *words)
        Assert.assertEquals("What about the red bike?", resultString)
    }

    @Test
    fun testPattern2() {

        val words = arrayOf("bike", "red")
        val resultString = java.lang.String.format("What about the %2\$s %1\$s?", *words)
        Assert.assertEquals("What about the red bike?", resultString)
    }

    @Test
    fun testPattern3() {

        var words: MutableList<Word> = mutableListOf()

        words.add(makeString("the"))
        words.add(makeNoun("bike"))
        words.add(makeVerb("is"))
        words.add(makeAdjective("red"))

        val pattern = Patterns.patterns[2]
        val resultString = pattern.toString(words.toList())
        Assert.assertNotNull(resultString)
        Assert.assertEquals("What about this red bike?", resultString)
    }

    @Test
    fun testPattern4() {

        var words: MutableList<Word> = mutableListOf()

        words.add(makeString("the"))
        words.add(makeString("this"))
        words.add(makeNoun("bike"))
        words.add(makeString("the"))
        words.add(makeVerb("is"))
        words.add(makeString("the"))
        words.add(makeAdjective("red"))

        val pattern = Patterns.patterns[2]
        val resultString = pattern.toString(words.toList())
        Assert.assertNotNull(resultString)
        Assert.assertEquals("What about this red bike?", resultString)
    }

    private fun makeString(s: String): Word {
        var string = WordString()
        string.wordString = s
        string.type = "string"
        return string
    }

    private fun makeAdjective(s: String): Word {
        var adjective = Adjective()
        adjective.type = "adjective"
        adjective.positive = s
        adjective.wordString = s
        return adjective
    }

    private fun makeVerb(s: String): Word {
        var verb = Verb()
        verb.type = "verb"
        verb.presentTense = s
        verb.wordString = s
        return verb
    }

    private fun makeNoun(s: String): Word {
        var noun = Noun()
        noun.type = "noun"
        noun.wordString = s
        return noun
    }
}
