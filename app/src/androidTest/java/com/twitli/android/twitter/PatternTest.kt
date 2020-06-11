/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import android.content.Intent
import android.provider.UserDictionary
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.dict.Pattern
import com.twitli.android.twitter.bot.dict.Patterns
import com.twitli.android.twitter.bot.dict.type.*
import com.twitli.android.twitter.bot.impl.ChatBotImpl
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
import com.twitli.android.twitter.dagger.TestComponent
import com.twitli.android.twitter.rule.InitPreferencesTestRule
import com.twitli.android.twitter.rule.MyDaggerMockLiveRule
import com.twitli.android.twitter.rule.MyDaggerMockRule
import com.twitli.android.twitter.twit.StatusImpl
import com.twitli.android.twitter.ui.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import twitter4j.Status
import twitter4j.StatusUpdate
import twitter4j.TwitterException
import javax.inject.Inject

class PatternTest {

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
    fun testPattern2() {

        var words: MutableList<List<Word>> = mutableListOf()

        words.add(makeAdjective("red"))
        words.add(makeNoun("bike"))

        val pattern = Patterns.patterns[1]
        Assert.assertTrue(pattern.matches(words))
    }

    @Test
    fun testPattern3() {

        var words: MutableList<List<Word>> = mutableListOf()

        words.add(makeNoun("bike"))
        words.add(makeVerb("is"))
        words.add(makeAdjective("red"))


        val pattern = Patterns.patterns[2]
        Assert.assertTrue(pattern.matches(words))
    }

    @Test
    fun testPattern4() {

        var words: MutableList<List<Word>> = mutableListOf()

        words.add(makeString("the"))
        words.add(makeNoun("bike"))
        words.add(makeVerb("is"))
        words.add(makeAdjective("red"))

        val pattern = Patterns.patterns[2]
        Assert.assertTrue(pattern.matches(words))
    }

    @Test
    fun testPattern5() {

        var words: MutableList<List<Word>> = mutableListOf()

        words.add(makeString("the"))
        words.add(makeNoun("bike"))
        words.add(makeVerb("is"))
        words.add(makeAdjectiveNoun("red"))

        val pattern = Patterns.patterns[1]
        Assert.assertFalse(pattern.matches(words))
    }

    @Test
    fun testPattern6() {

        var words: MutableList<List<Word>> = mutableListOf()

        words.add(makeString("the"))
        words.add(makeNoun("bike"))
        words.add(makeVerb("is"))
        words.add(makeAdjective("red"))

        val pattern = Patterns.patterns[1]
        Assert.assertFalse(pattern.matches(words))
    }

    @Test
    fun testPattern7(){
        var words: MutableList<List<Word>> = mutableListOf()

        words.add(makeString("the"))
        words.add(makeNoun("bike"))
        words.add(makeVerb("is"))
        words.add(makeAdjective("old"))
        words.add(makeString("and"))
        words.add(makeAdjective("red"))

        lateinit var pat : Pattern
        for(p in Patterns.patterns){
            if(p.matches(words)){
                pat = p
                break
            }
        }
        Assert.assertEquals(Patterns.patterns[2],pat)
    }

    private fun makeString(s: String): List<Word> {
        var the = WordString()
        the.wordString = s
        the.type = "string"
        var stringList = mutableListOf<WordString>()
        stringList.add(the)
        return stringList
    }

    private fun makeAdjective(s: String): List<Word> {
        var adjective = Adjective()
        adjective.type = "adjective"
        adjective.positive = s
        var adjectiveList = mutableListOf<Adjective>()
        adjectiveList.add(adjective)
        return adjectiveList.toList()

    }

    private fun makeVerb(s: String): List<Word> {
        var verb = Verb()
        verb.type = "verb"
        verb.presentTense = s
        verb.wordString = s
        var verbList = mutableListOf<Verb>()
        verbList.add(verb)
        return verbList.toList()
    }

    private fun makeNoun(s: String): List<Word> {
        var noun = Noun()
        noun.type = "noun"
        noun.wordString = s
        var nounList = mutableListOf<Word>()
        nounList.add(noun)
        return nounList.toList()
    }

    private fun makeAdjectiveNoun(s: String): List<Word> {
        var noun = Noun()
        noun.type = "noun"
        noun.wordString = s
        var nounList = mutableListOf<Word>()
        nounList.add(noun)
        var adjective = Adjective()
        adjective.type = "adjective"
        adjective.wordString = s
        nounList.add(adjective)
        return nounList.toList()
    }

}
