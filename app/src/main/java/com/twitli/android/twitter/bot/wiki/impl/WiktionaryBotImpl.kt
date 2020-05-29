/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.wiki.impl

import com.twitli.android.twitter.bot.wiki.*
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.bot.wiki.type.MyNumber
import com.twitli.android.twitter.bot.wiki.type.Noun
import com.twitli.android.twitter.bot.wiki.type.Word
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.SQLException
import java.util.*
import javax.inject.Inject

class WiktionaryBotImpl : WiktionaryBot {

    @Inject
    lateinit var dictionaryRepository: DictionaryRepository

    @Inject
    lateinit var wiktionaryApi: WiktionaryApi

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
    private fun getType(string: String): MutableList<Word> {

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
                        if ("Noun".equals(type, ignoreCase = true)) {
                            val n = Noun()
                            n.setSingular(string)
                            n.setWordString(string)
                            this.dictionaryRepository?.create(n)
                            words.add(n)
                        } else if ("Verb".equals(type, ignoreCase = true)) {
                            val v = Verb()
                            v.setPresentTense(string)
                            v.setPresentTenseThirdPersonSingular(string)
                            v.setWordString(string)
                            this.dictionaryRepository?.create(v)
                            words.add(v)
                        } else if ("Adverb".equals(type, ignoreCase = true)) {
                            val v = Adverb()
                            v.setPositive(string)
                            v.setWordString(string)
                            this.dictionaryRepository?.create(v)
                            words.add(v)
                        } else if ("Adjective".equals(type, ignoreCase = true)) {
                            val a = Adjective()
                            a.setPositive(string)
                            a.setWordString(string)
                            this.dictionaryRepository?.create(a)
                            words.add(a)
                        } else if ("Proper noun".equals(type, ignoreCase = true)) {
                            val a = ProperNoun()
                            a.setName(string)
                            a.setWordString(string)
                            this.dictionaryRepository?.create(a)
                            words.add(a)
                        } else if ("Conjunction".equals(type, ignoreCase = true)) {
                            val a = Conjunction()
                            a.setWordString(string)
                            this.dictionaryRepository?.create(a)
                            words.add(a)
                        } else if ("Interjection".equals(type, ignoreCase = true)) {
                            val a = Interjection()
                            a.setWordString(string)
                            this.dictionaryRepository?.create(a)
                            words.add(a)
                        } else if ("Proper noun".equals(type, ignoreCase = true)) {
                            val a = ProperNoun()
                            a.setName(string)
                            a.setWordString(string)
                            this.dictionaryRepository?.create(a)
                            words.add(a)
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
}
