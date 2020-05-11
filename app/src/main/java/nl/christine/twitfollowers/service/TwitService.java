/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import nl.christine.twitfollowers.MyApplication;
import nl.christine.twitfollowers.data.Content;
import nl.christine.twitfollowers.data.ContentRepository;
import nl.christine.twitfollowers.data.SettingsRepository;
import nl.christine.twitfollowers.data.UserRepository;
import nl.christine.twitfollowers.tweet.TwitManager;
import nl.christine.twitfollowers.wiki.WikiPageManager;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitService extends LifecycleService {

    private static final String LOGTAG = TwitService.class.getSimpleName();
    private SettingsRepository settingsRepository;
    private ContentRepository contentRepository;
    private UserRepository userRepository;

    @Inject
    WikiPageManager wikiPageManager;

    @Inject
    TwitManager twitManager;

    private ExecutorService es = Executors.newCachedThreadPool();
    private int year = 1821;
    private int currentYear;

    public class LocalBinder extends Binder {
        public TwitService getService() {
            return TwitService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ((MyApplication) getApplicationContext()).appComponent.inject(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentYear = calendar.get(Calendar.YEAR);

        contentRepository = new ContentRepository((getApplication()));
        settingsRepository = new SettingsRepository(getApplication());
        userRepository = new UserRepository(getApplication());

        es.execute(() -> {
            year = userRepository.getYear();
            Log.d(LOGTAG, "year = " + year);
        });

        userRepository.getFollowersCount().observeForever(followers -> {
            if (followers != null) {
                year = (int) (followers % currentYear);
                TwitService.this.doWiki(year);
            }
        });

        settingsRepository.isActive().observeForever(active -> {
            if (active != null) {
                if (active) {
                    TwitService.this.doWiki(year);
                }
            }
        });
    }

    private void tweet(Content content) {
        twitManager.tweet(content);
    }

    private void doWiki(int year) {

        es.execute(() -> {
            try {
                Content content = contentRepository.getFirst(year);
                if (content == null) {
                    wikiPageManager.getPage(Integer.toString(year));
                } else {
                    contentRepository.setDone(content.getId());
                    tweet(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
