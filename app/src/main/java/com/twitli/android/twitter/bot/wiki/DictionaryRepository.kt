package com.twitli.android.twitter.bot.wiki

import android.app.Application
import com.twitli.android.twitter.bot.wiki.type.*
import com.twitli.android.twitter.data.AppDatabase
import javax.inject.Inject

class DictionaryRepository @Inject constructor(application: Application?) {

    private val dictionaryDao: DictionaryDao

    init {
        val db: AppDatabase? = application?.let { AppDatabase.getDatabase(it) }
        dictionaryDao = db?.dictionaryDao()!!
    }

    fun create(n: Noun) {
        AppDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(n) }
    }

    fun create(v: Verb) {
        AppDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(v) }
    }

    fun create(a: Adverb) {
        AppDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(a) }
    }

    fun create(a: Adjective) {
        AppDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(a) }
    }

    fun create(a: ProperNoun) {
        TODO("Not yet implemented")
    }

    fun create(a: Conjunction) {
        TODO("Not yet implemented")
    }

    fun create(a: Interjection) {
        TODO("Not yet implemented")
    }

    fun create(a: MyNumber) {
        TODO("Not yet implemented")
    }

    fun getType(string: String): MutableList<Word> {
        var resultList: MutableList<Word> = mutableListOf()
        resultList.addAll(dictionaryDao.getNoun(string))
        resultList.addAll(dictionaryDao.getVerb(string))
        resultList.addAll(dictionaryDao.getAdjective(string))
        return resultList
    }

    fun create(word: WordString) {
        AppDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(word) }
    }

    fun clear() {
        dictionaryDao.clearNouns()
        dictionaryDao.clearVerbs()
        dictionaryDao.clearAdjectives()
    }
}
