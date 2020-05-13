/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.twitli.android.twitter.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs.getInt("inits", 0) > 2) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {

            setContentView(R.layout.activity_intro);

            (findViewById(R.id.submit)).setOnClickListener(v -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("inits", prefs.getInt("inits", 0) + 1);
                editor.commit();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }
    }
}
