/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.tweet;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.twitli.android.twitter.data.AppDatabase;
import twitter4j.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitRepository {

    private static final String LOGTAG = TwitRepository.class.getSimpleName();
    private TwitDao twitDao;
    private LiveData<List<Tweet>> tweets;
    ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

    @Inject
    public TwitRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        twitDao = db.twitDao();
        tweets = twitDao.getTweets();

        es.scheduleAtFixedRate(() -> {
            try {
                loadTweets();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }, 20, 120, SECONDS);
    }

    public void create(final Tweet tweet) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            twitDao.create(tweet);
        });
    }

    public void loadTweets() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Paging paging = new Paging();
        paging.setCount(20);
        List<Status> tweets = twitter.getHomeTimeline(paging);
        for (Status status : tweets) {
            twitDao.store(status.getId(), status.getUser().getName(), status.getUser().getScreenName(), status.getCreatedAt().getTime(), status.getText());
        }
    }
    public LiveData<List<Tweet>> getTweets() {
        return tweets;

    }

    public void clear() {
        twitDao.clear();
    }

    public LiveData<Tweet> getTweet(int id) {
        return twitDao.getTweet(id);
    }
}
