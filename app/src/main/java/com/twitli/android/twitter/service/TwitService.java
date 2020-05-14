/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.service;

import android.content.*;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.R;
import com.twitli.android.twitter.data.*;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.tweet.TwitManagerImpl;
import com.twitli.android.twitter.tweet.TwitRepository;
import com.twitli.android.twitter.wiki.WikiPageManager;
import org.apache.commons.lang3.math.NumberUtils;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitService extends LifecycleService {

    private static final String LOGTAG = TwitService.class.getSimpleName();
    private SettingsRepository settingsRepository;
    private ContentRepository contentRepository;
    private UserRepository userRepository;
    private TwitRepository twitRepository;

    private long init = 10l;
    long count = 999l;
    private List<String> intervals;
    private boolean active = false;

    @Inject
    WikiPageManager wikiPageManager;

    @Inject
    TwitManager twitManager;

    private ScheduledExecutorService es = Executors.newScheduledThreadPool(2);
    private int year = 42;
    private int currentYear;
    private long tweetedHistory = System.currentTimeMillis();

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

        Log.d(LOGTAG, "TwitService started");

        ((MyApplication) getApplicationContext()).appComponent.inject(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentYear = calendar.get(Calendar.YEAR);

        contentRepository = new ContentRepository((getApplication()));
        settingsRepository = new SettingsRepository(getApplication());
        userRepository = new UserRepository(getApplication());
        twitRepository = new TwitRepository(getApplication());

        es.execute(() -> {
            year = userRepository.getYear();
            Log.d(LOGTAG, "year = " + year);
        });

        userRepository.getFollowersCount().observeForever(followers -> {

            Log.d(LOGTAG, "followers count is " + followers != null ? Long.toString(followers) : "null");
            if (followers != null && followers != 0) {
                year = (int) (followers % currentYear);
                Log.d(LOGTAG, "doWiki 111 " + year);
                TwitService.this.doWiki(year);
            }
        });

        settingsRepository.isActive().observeForever(active -> {
            if (active != null) {
                if (active) {
                    if (System.currentTimeMillis() - tweetedHistory > 900000l) {
                        Log.d(LOGTAG, "doWiki 121 " + year);
                        TwitService.this.doWiki(year);
                    }
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        intervals = Arrays.asList(getApplicationContext().getResources().getStringArray(R.array.tweet_interval));

        settingsRepository.isActive().observeForever(active -> {
            Log.d(LOGTAG, "active = " + this.active + " " + active);
            if (active != null) {
                TwitService.this.active = active;
            }
        });

        es.scheduleAtFixedRate(() -> {
            Log.d(LOGTAG, "long interval = " + getLongInterval(prefs.getInt("tweet_interval", 2)));
            if (count > getLongInterval(prefs.getInt("tweet_interval", 2)) - 0.1) {
                count = 0l;
                if (active) {
                    Log.d(LOGTAG, "getUser() called");
                    getUser();
                }
            } else {
                count = count + 1l;
            }
        }, init, 3600l, SECONDS);

        IntentFilter filter = new IntentFilter("nl.christine.app.message");
        filter.addAction("com.twitli.app.message");
        registerReceiver(receiver, filter);

        es.schedule(() -> {
            cleanupTwitRepository();
        }, 60, SECONDS);
    }

    private void cleanupTwitRepository() {
        twitRepository.cleanUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d(LOGTAG, "TwitService stopped");
    }

    private void tweet(Content content) {
        Log.d(LOGTAG, "tweet " + content.getYear() + " " + content.getText());
        twitManager.tweet(content);
    }

    private int getUser() {
        try {
            User user = twitManager.verifyCredentials();
            userRepository.persist(user);
            // userRepository.setFollowers(user.getFollowersCount());
            return user.getFollowersCount();
        } catch (TwitterException e) {
            e.printStackTrace();
            return 1600;
        }
    }


    private void doWiki(int year) {
        Log.d(LOGTAG, "doWiki 179 " + year);
        es.execute(() -> {

            tweetedHistory = System.currentTimeMillis();
            ContentStatus contentStatus = contentRepository.getStatus(year);
            switch (contentStatus) {

                case AVAILABLE:
                    Log.d(LOGTAG, "AVAILABLE");
                    Content content = contentRepository.getFirst(Integer.toString(year));
                    if (content != null) {
                        contentRepository.setDone(content.getId());
                        Log.d(LOGTAG, "setDone " + content.getText());
                        tweet(content);
                    } else {
                        Log.e(LOGTAG, "no content found " + year);
                    }
                    break;

                case NONE:
                    Log.d(LOGTAG, "NONE");
                    try {
                        wikiPageManager.getPage(Integer.toString(year));
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

    private long getLongInterval(int position) {
        String interval = intervals.get(position);
        interval = interval.substring(0, interval.indexOf(" "));
        if (NumberUtils.isCreatable(interval)) {
            return Integer.parseInt(interval);
        }
        return 1l;
    }

}
