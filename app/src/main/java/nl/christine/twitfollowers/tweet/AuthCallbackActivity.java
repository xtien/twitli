/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.tweet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import nl.christine.twitfollowers.R;
import nl.christine.twitfollowers.ui.MainActivity;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthCallbackActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Uri data = getIntent().getData();

        String token = data.getQueryParameter("oauth_token");
        String verifier = data.getQueryParameter("oauth_verifier");


        SharedPreferences.Editor edit = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        edit.putString("access_token", token);
        edit.putString("access_token_verifier", verifier);
        edit.commit();

        startActivity(new Intent(this, MainActivity.class));
    }
}
