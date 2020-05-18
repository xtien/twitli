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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TwitManagerImpl implements TwitManager {

    private static final String LOGTAG = TwitManagerImpl.class.getSimpleName();
    private final Twitter twitter;
    private final Application application;
    ExecutorService es = Executors.newCachedThreadPool();

    public TwitManagerImpl() {

        application = MyApplication.getApplication();

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

    @Override
    public void tweet(Content content) {
        String string = content.getYear() + ", " + (content.getDate() != null ? (content.getDate() + ". ") : "") + content.getText();
        tweet(string);
    }

    @Override
    public void reply(String string, Long replyToId){
        tweetIt(string,replyToId);
    }

    @Override
    public void tweet(String string) {
        tweetIt(string, null);
     }

    private void tweetIt(String string, Long replyToId) {
        es.execute(() -> {
            try {
                String twitString = string.length() > 280 ? string.substring(0, 280) : string;
                if (twitString.length() > 280) {
                    twitString = twitString.substring(0, twitString.lastIndexOf(" "));
                    if (twitString.length() < 275) {
                        twitString += " ...";
                    }
                }
                StatusUpdate statusUpdate = new StatusUpdate(twitString);
                if(replyToId !=null){
                    statusUpdate.setInReplyToStatusId(replyToId);
                }

                twitter.updateStatus(statusUpdate);
                Log.d(LOGTAG, "status " + twitString);
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
    public User verifyCredentials() throws TwitterException {
        return twitter.verifyCredentials();
    }

    @Override
    public void like(Long tweetId) {
        try {
            twitter.createFavorite(tweetId);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Status> getHomeTimeline(Paging paging) throws TwitterException {
            return twitter.getHomeTimeline(paging);
    }
}
