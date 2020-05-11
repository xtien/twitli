/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitli.ui;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import nl.christine.twitli.MyApplication;
import nl.christine.twitli.R;
import nl.christine.twitli.service.TwitService;
import nl.christine.twitli.tweet.TwitManager;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = MainActivity.class.getSimpleName();
    private TwitService twitService;
    private ExecutorService es = Executors.newCachedThreadPool();

    @Inject
    public TwitManager twitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        ((MyApplication) getApplicationContext()).appComponent.inject(this);

        Intent icycle = new Intent(this, TwitService.class);
        bindService(icycle, connection, Context.BIND_AUTO_CREATE);

        String accessToken = prefs.getString("access_token", null);
        String accessTokenVerifier = prefs.getString("access_token_verifier", null);
        String accesTokenSecret = prefs.getString("access_token_secret", null);

        if (accessTokenVerifier != null && accesTokenSecret == null) {

            es.execute(() -> {
                twitManager.createAccessToken(accessTokenVerifier);
            });

        } else if (accessToken == null) {

            es.execute(() -> {

                RequestToken requestToken = null;
                try {
                    requestToken = twitManager.createRequestToken();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthorizationURL()));
                startActivity(browserIntent);
            });
        }
    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            TwitService.LocalBinder binder = (TwitService.LocalBinder) service;
            twitService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

}
