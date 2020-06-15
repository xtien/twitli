/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.bot.dict

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.twitli.android.twitter.bot.dict.type.*
import java.util.concurrent.Executors

@Database(entities = [
    Noun::class,
    Verb::class,
    Adjective::class,
    Adverb::class,
    PersonalPronoun::class,
    WordString::class,
    MyNumber::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DictDatabase : RoomDatabase() {

    abstract fun dictionaryDao(): DictionaryDao

    companion object {

        @Volatile
        private lateinit var INSTANCE: DictDatabase
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)!!

        private val databaseCallback: Callback = object : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    INSTANCE.dictionaryDao().insertAll(PersonalPronouns.populateData())
                }
            }
        }

        fun getDatabase(context: Context): DictDatabase? {
            if (!this::INSTANCE.isInitialized) {
                synchronized(DictDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            DictDatabase::class.java, "dict_database")
                            .addCallback(databaseCallback)
                            .build()
                }
            }
            return INSTANCE
        }
    }
}
