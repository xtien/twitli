/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.ui;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.R;
import com.twitli.android.twitter.service.TwitService;
import com.twitli.android.twitter.tweet.TwitFragment;
import com.twitli.android.twitter.tweet.TwitManager;
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

    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

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

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(MainFragment.newInstance());
        pagerAdapter.addFragment(TwitFragment.newInstance());
        viewPager.setAdapter(pagerAdapter);
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
