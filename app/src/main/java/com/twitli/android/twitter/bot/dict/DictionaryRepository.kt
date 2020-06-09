/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

import android.app.Application
import com.twitli.android.twitter.bot.dict.type.*
import com.twitli.android.twitter.bot.dict.type.Interjection
import com.twitli.android.twitter.bot.dict.type.ProperNoun
import javax.inject.Inject

class DictionaryRepository @Inject constructor(application: Application?) {

    private val dictionaryDao: DictionaryDao

    init {
        val db: DictDatabase? = application?.let { DictDatabase.getDatabase(it) }
        dictionaryDao = db?.dictionaryDao()!!
    }

    fun create(n: Noun) {
        DictDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(n) }
    }

    fun create(v: Verb) {
        DictDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(v) }
    }

    fun create(a: Adverb) {
        DictDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(a) }
    }

    fun create(a: Adjective) {
        DictDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(a) }
    }

    fun getType(string: String): MutableList<Word> {
        var resultList: MutableList<Word> = mutableListOf()
        resultList.addAll(dictionaryDao.getNoun(string))
        resultList.addAll(dictionaryDao.getVerb(string))
        resultList.addAll(dictionaryDao.getAdjective(string))
        resultList.addAll(dictionaryDao.getPersonalPronoun(string))
        return resultList
    }

    fun create(word: WordString) {
        DictDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(word) }
    }

    fun clear() {
        dictionaryDao.clearNouns()
        dictionaryDao.clearVerbs()
        dictionaryDao.clearAdjectives()
    }

    fun create(personalPronoun: PersonalPronoun) {
        dictionaryDao.createOrUpdate(personalPronoun)
    }

    fun getPersonalPronouns(): List<PersonalPronoun> {
        return dictionaryDao.getPersonalPronouns()
    }

    fun getNouns(): List<Noun> {
        return dictionaryDao.getNouns()
    }
}
