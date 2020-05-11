/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.data.*;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.wiki.WikiPageManager;

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

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");
            es.execute(() -> {
                try {
                    String text = wikiPageManager.getPage(message);
                    twitManager.tweet(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    };

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
            if (followers != null && followers != 0) {
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

        IntentFilter filter = new IntentFilter("nl.christine.app.message");
        filter.addAction("com.twitli.app.message");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void tweet(Content content) {
        twitManager.tweet(content);
    }

    private void doWiki(int year) {
        es.execute(() -> {

            ContentStatus contentStatus = contentRepository.getStatus(year);
            switch (contentStatus) {

                case AVAILABLE:
                    Log.d(LOGTAG, "AVAILABLE");
                    Content content = contentRepository.getFirst(year);
                    if (content != null) {
                        contentRepository.setDone(content.getId());
                        tweet(content);
                    } else {
                        Log.e(LOGTAG, "no content found " + year);
                    }
                    break;

                case NONE:
                    Log.d(LOGTAG, "NONE");
                    try {
                        String text = wikiPageManager.getPage(Integer.toString(year));
                     } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case DONE:
                    Log.d(LOGTAG, "DONE");
                    // All items already displayed, nothing to do here.
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
