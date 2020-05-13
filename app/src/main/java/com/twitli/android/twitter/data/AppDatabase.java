/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.twitli.android.twitter.tweet.Tweet;
import com.twitli.android.twitter.tweet.TwitDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MySettings.class, Content.class, User.class, Tweet.class},  version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE settings_table "
                    + " ADD COLUMN settingsid INTEGER DEFAULT 0 NOT NULL");
        }
    };

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract SettingsDao settingsDao();
    public abstract ContentDao contentDao();
    public abstract UserDao userDao();
    public abstract TwitDao twitDao();

    private static Callback databaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                SettingsDao settingsDao = INSTANCE.settingsDao();
                MySettings settings = new MySettings();
                settings.setId(0);
                settingsDao.insert(settings);

                UserDao userDao = INSTANCE.userDao();
                User user = new User();
                user.setMe(true);
                userDao.insert(user);
            });
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(databaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
