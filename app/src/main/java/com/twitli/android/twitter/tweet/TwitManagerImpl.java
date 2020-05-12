/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.tweet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.R;
import com.twitli.android.twitter.data.Content;
import com.twitli.android.twitter.data.SettingsRepository;
import com.twitli.android.twitter.data.UserRepository;
import org.apache.commons.lang3.math.NumberUtils;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitManagerImpl implements TwitManager {

    private static final String LOGTAG = TwitManagerImpl.class.getSimpleName();
    private final Twitter twitter;
    private final Application application;
    private final List<String> intervals;
    private SettingsRepository settingsRepository;
    private long tweetIntervalHours = 1;
    private long init = 20l;

    ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
    private boolean active;
    private UserRepository userRepository;
    long count = 9999l;

    public TwitManagerImpl() {

        application = MyApplication.getApplication();
        settingsRepository = new SettingsRepository(application);
        userRepository = new UserRepository(application);

        SharedPreferences prefs = application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int position = prefs.getInt("tweet_interval", 0);
        intervals = Arrays.asList(application.getApplicationContext().getResources().getStringArray(R.array.tweet_interval));
        tweetIntervalHours = getLongInterval(position);
        settingsRepository.isActive().observeForever(active -> {
            Log.d(LOGTAG, "active = " + this.active + " " + active);
            if (active != null) {
                TwitManagerImpl.this.active = active;
                if(active){
                    tweetIntervalHours = 999l;
                }
            }
        });

        es.scheduleAtFixedRate(() -> {

            if (count >= tweetIntervalHours) {
                count = 0l;
                if (active) {
                    getUser();
                }
            } else {
                count = count + 1l;
            }
        }, init, 3600l, SECONDS);

        String accessTokenKey = prefs.getString("access_token", null);
        String accesTokenSecret = prefs.getString("access_token_secret", null);
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(application.getString(R.string.api_key), application.getString(R.string.api_secret));
        if (accessTokenKey != null) {
            AccessToken accessToken = new AccessToken(accessTokenKey, accesTokenSecret);
            twitter.setOAuthAccessToken(accessToken);
        }
    }

    private int getUser() {
        try {
            User user = twitter.verifyCredentials();
            userRepository.persist(user);
            // userRepository.setFollowers(user.getFollowersCount());
            return user.getFollowersCount();
        } catch (TwitterException e) {
            e.printStackTrace();
            return 1600;
        }
    }

    @Override
    public void tweet(Content content) {
        String string = content.getYear() + ", " + (content.getDate() != null ? (content.getDate() + ". ") : "") + content.getText();
        tweet(string);
    }

    @Override
    public void tweet(String string) {

        Log.d(LOGTAG, "status " + string);

        es.execute(() -> {
            try {
                twitter.updateStatus(string.length() > 280 ? string.substring(0, 280) : string);
            } catch (TwitterException e) {
                int statusCode = e.getStatusCode();
                if (statusCode == 403) {
                    String message = e.getErrorMessage();
                    Log.e(LOGTAG, "statusCode " + statusCode + " " + message);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void createAccessToken(String accessTokenVerifier) {

        SharedPreferences prefs = application.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        try {
            RequestToken requestToken = new RequestToken(prefs.getString("request_token", null), prefs.getString("request_token_secret", null));

            if (requestToken != null) {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, accessTokenVerifier);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("access_token_secret", accessToken.getTokenSecret());
                editor.putString("access_token", accessToken.getToken());
                editor.putLong("user_id", accessToken.getUserId());
                editor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public RequestToken createRequestToken() throws TwitterException {
        RequestToken requestToken = twitter.getOAuthRequestToken();
        SharedPreferences.Editor editor = application.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
        editor.putString("request_token", requestToken.getToken());
        editor.putString("request_token_secret", requestToken.getTokenSecret());
        editor.commit();
        return requestToken;
    }

    @Override
    public void setInterval(int position) {
        tweetIntervalHours = getLongInterval(position);
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
