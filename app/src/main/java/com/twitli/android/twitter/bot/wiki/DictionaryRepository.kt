package com.twitli.android.twitter.bot.wiki

import android.app.Application
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.wiki.type.MyNumber
import com.twitli.android.twitter.bot.wiki.type.Noun
import com.twitli.android.twitter.bot.wiki.type.Word
import com.twitli.android.twitter.data.AppDatabase
import com.twitli.android.twitter.tweet.TwitManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

class DictionaryRepository @Inject constructor(application: Application?) {

    private val dictionaryDao: DictionaryDao

    init {
        val db: AppDatabase? = application?.let { AppDatabase.getDatabase(it) }
        dictionaryDao = db?.dictionaryDao()!!
    }

    fun create(n: Noun) {
        AppDatabase.databaseWriteExecutor.execute { dictionaryDao.createOrUpdate(n) }
    }

    fun create(n: Verb) {
        TODO("Not yet implemented")
    }

    fun create(v: Adverb) {
        TODO("Not yet implemented")
    }

    fun create(a: Adjective) {
        TODO("Not yet implemented")
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
}
