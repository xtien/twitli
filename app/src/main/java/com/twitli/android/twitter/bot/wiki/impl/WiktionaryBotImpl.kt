/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.wiki.impl

import android.app.Application
import android.content.Context
import android.util.Log
import com.twitli.android.twitter.R
import com.twitli.android.twitter.bot.dict.DictionaryRepository
import com.twitli.android.twitter.bot.dict.type.*
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.WiktionaryPage
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.tweet.Tweet
import org.apache.commons.lang3.math.NumberUtils
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.SQLException
import java.util.*
import javax.inject.Inject

class WiktionaryBotImpl @Inject constructor(app: Application, dictionaryRepository: DictionaryRepository, wiktionaryApi: WiktionaryApi) : WiktionaryBot {

    private val LOGTAG = javaClass.name

    private var dictionaryRepository: DictionaryRepository = dictionaryRepository
    private var context: Context = app
    private var wiktionaryApi: WiktionaryApi = wiktionaryApi

    var formOfDefinitionString = "form-of-definition-link"
    var ref = "href=\"/wiki/%s#english"
    var ref2 = "href=\"//en.wikipedia.org/wiki/%s"
    var ref3 = "href=\"https://en.wiktionary.org/wiki/%s"
    var hRefWikiString = "href=\"/wiki/"
    var noEntry = "wiktionary does not yet have an entry for "

    init {
        formOfDefinitionString = context.getString(R.string.form_of_definition_link)
        ref = context.getString(R.string.wiki_ref)
        ref2 = context.getString(R.string.wiki_ref2)
        ref3 = context.getString(R.string.wiki_ref3)
        hRefWikiString = context.getString(R.string.h_ref_wiki_string)
        noEntry = context.getString(R.string.no_entry)
    }

    @Throws(IOException::class, SQLException::class)
    fun getTypeLocal(string: String): MutableList<Word> {
        return dictionaryRepository.getType(string)
    }

    /*
     FileNotFoundException if page does not exist in wiktionary
    */
    @Throws(IOException::class, SQLException::class)
    override fun getType(string: String): MutableList<Word> {

        val localResult = getTypeLocal(string)
        if (localResult.isNotEmpty()) {
            return localResult
        }

        var stringD = string.decapitalize()
        val words: MutableList<Word> = ArrayList<Word>()
        val refString = String.format(ref, stringD).toLowerCase()
        val refString2 = String.format(ref2, stringD).toLowerCase()
        val refString3 = String.format(ref3, stringD).toLowerCase()

        if (NumberUtils.isCreatable(string)) {
            val n = MyNumber()
            n.wordString = string
            n.type = "number"
            this.dictionaryRepository.create(n)
            words.add(n)
        } else {


            try {
                var wikiPage: String? = this.wiktionaryApi.getWikiWord(stringD)?.toLowerCase()
                if (wikiPage != null) {

                    if (!wikiPage.contains(refString) && !wikiPage.contains(refString2) && !wikiPage.contains(refString3)) {
                        if (wikiPage.contains(noEntry)) {
                            return words
                        }
                        stringD = wikiPage.substring(wikiPage.indexOf(formOfDefinitionString) + formOfDefinitionString.length, wikiPage.length)
                        stringD = stringD.substring(stringD.indexOf(hRefWikiString) + hRefWikiString.length, stringD.length)
                        stringD = stringD.substring(0, stringD.indexOf("#")).toLowerCase()
                        wikiPage = this.wiktionaryApi.getWikiWord(stringD)
                    }

                    val page = WiktionaryPage(wikiPage!!)
                    val types: List<String> = page.parseForWord()

                    if (!types.contains("article")) {
                        for (type in types) {

                            when {
                                "noun".equals(type, ignoreCase = true) -> {
                                    val n = Noun()
                                    n.singular = stringD
                                    n.wordString = stringD
                                    n.type = type
                                    this.dictionaryRepository.create(n)
                                    words.add(n)
                                }
                                "verb".equals(type, ignoreCase = true) -> {
                                    val v = Verb()
                                    v.presentTense = stringD
                                    v.presentTenseThirdPersonSingular = stringD
                                    v.wordString = stringD
                                    v.type = type
                                    this.dictionaryRepository.create(v)
                                    words.add(v)
                                }
                                "adverb".equals(type, ignoreCase = true) -> {
                                    val v = Adverb()
                                    v.positive = stringD
                                    v.wordString = stringD
                                    v.type = type
                                    this.dictionaryRepository.create(v)
                                    words.add(v)
                                }
                                "adjective".equals(type, ignoreCase = true) -> {
                                    val a = Adjective()
                                    a.positive = stringD
                                    a.wordString = stringD
                                    a.type = type
                                    this.dictionaryRepository.create(a)
                                    words.add(a)
                                }
                            }
                        }
                    }
                }
            } catch (fnfe: FileNotFoundException) {
                Log.d(LOGTAG, "word not found " + string)
            }
        }

        if (words.isEmpty()) {
            val w = WordString()
            w.wordString = stringD
            words.add(w)
            dictionaryRepository.create(w)
        }

        return words
    }

    override fun classify(string: String): List<Word> {

        var words: MutableList<Word> = ArrayList<Word>()

        if (string.length > 2 && !string.matches(Regex(".*\\d.*"))) {
            words = getType(string)
        }

        return words.toList()
    }

    override fun getWords(status: Tweet): List<List<Word>> {
        var list: MutableList<List<Word>> = mutableListOf()
        var strings = status.text!!.split(" ")
        for (string in strings) {
            val type = getType(string)
            list.add(type.toList())
        }
        return list.toList()
    }
}
