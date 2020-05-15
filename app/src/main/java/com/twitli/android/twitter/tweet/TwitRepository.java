/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.tweet;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.twitli.android.twitter.data.AppDatabase;
import twitter4j.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitRepository {

    private static final String LOGTAG = TwitRepository.class.getSimpleName();
    private static final long ONEHOUR = 3600000l;
    private TwitDao twitDao;
    private LiveData<List<Tweet>> tweets;
    ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

    @Inject
    public TwitRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        twitDao = db.twitDao();
        tweets = twitDao.getTweets();

        es.scheduleAtFixedRate(() -> {
                loadTweets();
         }, 20, 120, SECONDS);
    }

    public void create(final Tweet tweet) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            twitDao.create(tweet);
        });
    }

    public void loadTweets()  {
        Twitter twitter = TwitterFactory.getSingleton();
        Paging paging = new Paging();
        paging.setCount(20);
        List<Status> tweets = new ArrayList<>();
        try{
            tweets = twitter.getHomeTimeline(paging);
        } catch (TwitterException e){
            Log.e(LOGTAG, "Twitter error " +e.getExceptionCode() + " " +  e.getMessage());
        }
        for (Status status : tweets) {
            twitDao.store(status.getId(), status.getUser().getName(), status.getUser().getScreenName(), status.getCreatedAt().getTime(), status.getText(), status.isFavorited(), status.getFavoriteCount());
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

    public void cleanUp() {
        twitDao.cleanUp(System.currentTimeMillis() - ONEHOUR);
    }
}
