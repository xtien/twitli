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
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitManagerImpl implements TwitManager {

    private static final String LOGTAG = TwitManagerImpl.class.getSimpleName();
    private final Twitter twitter;
    private final Application application;
    private SettingsRepository settingsRepository;
    private long tweetInterval = 3600l;
    private long init = 20l;

    ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
    private boolean active;
    private UserRepository userRepository;

    public TwitManagerImpl() {

        application = MyApplication.getApplication();
        settingsRepository = new SettingsRepository(application);
        userRepository = new UserRepository(application);

        tweetInterval = application.getSharedPreferences("prefs", Context.MODE_PRIVATE).getLong("tweet_interval", 3600l);

        settingsRepository.isActive().observeForever(active -> {
            Log.d(LOGTAG, "active = " + this.active + " " + active);
            if (active != null) {
                TwitManagerImpl.this.active = active;
            }
        });

        SharedPreferences prefs = application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int position = prefs.getInt("tweet_interval", 0);
        tweetInterval = setTweetInterval(position);

        es.scheduleAtFixedRate(() -> {
            if (active) {
                getUser();
            }
        }, init, tweetInterval, SECONDS);

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
        tweetInterval = setTweetInterval(position);
    }

    private long setTweetInterval(int position) {
        long tweetInterval = this.tweetInterval;
        switch (position) {
            case 0:
                tweetInterval = 3600l;
                break;
            case 1:
                tweetInterval = 21600l;
                break;
            case 2:
                tweetInterval = 144000l;
                break;
        }
        return tweetInterval;
    }
}
