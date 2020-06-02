/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.wiki.impl

import com.twitli.android.twitter.bot.wiki.*
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.bot.wiki.type.*
import twitter4j.Status
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.SQLException
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

class WiktionaryBotImpl @Inject constructor(dictionaryRepository: DictionaryRepository, wiktionaryApi: WiktionaryApi) : WiktionaryBot {

    private var dictionaryRepository: DictionaryRepository = dictionaryRepository

    private var wiktionaryApi: WiktionaryApi = wiktionaryApi

    var formOfDefinitionString = "form-of-definition-link"
    var ref = "href=\"/wiki/%s#english"
    var ref2 = "href=\"//en.wikipedia.org/wiki/%s"
    var ref3 = "href=\"https://en.wiktionary.org/wiki/%s"
    var hRefWikiString = "href=\"/wiki/"
    var noEntry = "wiktionary does not yet have an entry for "

    /*
   FileNotFoundException if page does not exist in wiktionary
    */
    @Throws(IOException::class, SQLException::class)
    override fun getType(string: String): MutableList<Word> {

        var string = string
        val words: MutableList<Word> = ArrayList<Word>()
        val refString = String.format(ref, string).toLowerCase()
        val refString2 = String.format(ref2, string).toLowerCase()
        val refString3 = String.format(ref3, string).toLowerCase()

        try {
            var wikiPage: String? = this.wiktionaryApi?.getWikiWord(string)?.toLowerCase()
            if (wikiPage != null) {

                if (!wikiPage.contains(refString) && !wikiPage.contains(refString2) && !wikiPage.contains(refString3)) {
                    if (wikiPage.contains(noEntry)) {
                        return words
                    }
                    string = wikiPage.substring(wikiPage.indexOf(formOfDefinitionString) + formOfDefinitionString.length, wikiPage.length)
                    string = string.substring(string.indexOf(hRefWikiString) + hRefWikiString.length, string.length)
                    string = string.substring(0, string.indexOf("#"))
                    wikiPage = this.wiktionaryApi?.getWikiWord(string)
                }

                val page = WiktionaryPage(wikiPage!!)
                val types: List<String> = page.parseForWord()

                if (types != null) {
                    for (type in types) {
                        when {
                            "Noun".equals(type, ignoreCase = true) -> {
                                val n = Noun()
                                n.setSingular(string)
                                n.setWordString(string)
                                n.setType(type)
                                this.dictionaryRepository?.create(n)
                                words.add(n)
                            }
                            "Verb".equals(type, ignoreCase = true) -> {
                                val v = Verb()
                                v.presentTense = string
                                v.presentTenseThirdPersonSingular = string
                                v.setWordString(string)
                                v.setType(type)
                                this.dictionaryRepository?.create(v)
                                words.add(v)
                            }
                            "Adverb".equals(type, ignoreCase = true) -> {
                                val v = Adverb()
                                v.positive = string
                                v.setWordString(string)
                                v.setType(type)
                                this.dictionaryRepository?.create(v)
                                words.add(v)
                            }
                            "Adjective".equals(type, ignoreCase = true) -> {
                                val a = Adjective()
                                a.positive = string
                                a.setWordString(string)
                                a.setType(type)
                                this.dictionaryRepository?.create(a)
                                words.add(a)
                            }
                            "Proper noun".equals(type, ignoreCase = true) -> {
                                val a = ProperNoun()
                                a.setName(string)
                                a.setWordString(string)
                                a.setType(type)
                                this.dictionaryRepository?.create(a)
                                words.add(a)
                            }
                            "Conjunction".equals(type, ignoreCase = true) -> {
                                val a = Conjunction()
                                a.setWordString(string)
                                a.setType(type)
                                this.dictionaryRepository?.create(a)
                                words.add(a)
                            }
                            "Interjection".equals(type, ignoreCase = true) -> {
                                val a = Interjection()
                                a.setWordString(string)
                                a.setType(type)
                                this.dictionaryRepository?.create(a)
                                words.add(a)
                            }
                            "Proper noun".equals(type, ignoreCase = true) -> {
                                val a = ProperNoun()
                                a.setName(string)
                                a.setWordString(string)
                                a.setType(type)
                                this.dictionaryRepository?.create(a)
                                words.add(a)
                            }
                        }
                    }
                }
            }
        } catch (fnfe: FileNotFoundException) {
        }
        return words
    }

    override fun classify(string: String): List<Word> {

        var words: MutableList<Word> = ArrayList<Word>()

        if (string != null && string.length > 2 && !string.matches(Regex(".*\\d.*"))) {
            words = getType(string)
        }

        if (words == null || words.size == 0) {
            if (string != null && string.matches(Regex(".*\\d.*"))) {
                val a = MyNumber()
                if (string != null) {
                    a.setWordString(string)
                }
                this.dictionaryRepository?.create(a)
                words.add(a)
            }
        }
        return words
    }

    override fun getNouns(status: Status): List<String> {
        var list = status.text.split(" ").stream().filter { s -> (s != null && isNoun(s)) }.collect(Collectors.toList())
        return list
    }

    private fun isNoun(s: String): Boolean {
        return !classify(s).stream().filter { w -> "noun" == w.getType() }.collect(Collectors.toList()).isEmpty()
    }
}
