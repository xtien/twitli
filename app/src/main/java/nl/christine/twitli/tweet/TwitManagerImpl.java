/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitli.tweet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import nl.christine.twitli.MyApplication;
import nl.christine.twitli.R;
import nl.christine.twitli.data.Content;
import nl.christine.twitli.data.SettingsRepository;
import nl.christine.twitli.data.UserRepository;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitManagerImpl implements TwitManager {

    private final Twitter twitter;
    private final Application application;
    private SettingsRepository settingsRepository;
    private long period = 900l;
    private long init = 20l;
    private int followers = 0;

    ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
    private boolean active;
    private UserRepository userRepository;

    public TwitManagerImpl() {

        application = MyApplication.getApplication();
        settingsRepository = new SettingsRepository(application);
        userRepository = new UserRepository(application);

        settingsRepository.isActive().observeForever(active -> {
            if (active != null) {
                TwitManagerImpl.this.active = active;
            }
        });

        es.scheduleAtFixedRate(() -> {
            if (active) {
                int newFollowers = getFollowers();
                if (newFollowers != followers) {
                    followers = newFollowers;
                }
            }
        }, init, period, SECONDS);

        SharedPreferences prefs = application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String accessTokenKey = prefs.getString("access_token", null);
        String accesTokenSecret = prefs.getString("access_token_secret", null);
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(application.getString(R.string.api_key), application.getString(R.string.api_secret));
        if (accessTokenKey != null) {
            AccessToken accessToken = new AccessToken(accessTokenKey, accesTokenSecret);
            twitter.setOAuthAccessToken(accessToken);
        }
    }

    private int getFollowers() {
        try {
            User user = twitter.verifyCredentials();
            userRepository.persist(user);
            return user.getFollowersCount();
        } catch (TwitterException e) {
            e.printStackTrace();
            return 1600;
        }
    }

    @Override
    public void tweet(Content content) {
        tweet(content.getYear() + ", " + content.getDate() != null ? (content.getDate() + ". ") : "" + content.getText());
    }

    @Override
    public void tweet(String string) {
        es.execute(() -> {
            try {
                twitter.updateStatus(string.length() > 280 ? string.substring(0, 280) : string);
            } catch (TwitterException e) {
                e.printStackTrace();
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
}
