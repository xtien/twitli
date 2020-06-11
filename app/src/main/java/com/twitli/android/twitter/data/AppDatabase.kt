/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.twitli.android.twitter.tweet.Tweet
import com.twitli.android.twitter.tweet.TwitDao
import java.util.concurrent.Executors

@Database(entities = [
    MySettings::class,
    Content::class,
    User::class,
    Tweet::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao
    abstract fun contentDao(): ContentDao
    abstract fun userDao(): UserDao
    abstract fun twitDao(): TwitDao

    companion object {

        @Volatile
        private lateinit var INSTANCE: AppDatabase
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)!!
        private val databaseCallback: Callback = object : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    val settingsDao = INSTANCE.settingsDao()
                    val settings = MySettings()
                    settings.id = 0
                    settingsDao.insert(settings)
                    val userDao = INSTANCE.userDao()
                    val user = User()
                    user.isMe = true
                    userDao.insert(user)
                }
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            if (!this::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "app_database")
                            .addCallback(databaseCallback)
                            .build()
                }
            }
            return INSTANCE
        }
    }
}
