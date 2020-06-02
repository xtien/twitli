/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.tweet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twitli.android.twitter.R
import com.twitli.android.twitter.ui.MainActivity

class AuthCallbackActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val data = intent.data
        val token = data!!.getQueryParameter("oauth_token")
        val verifier = data.getQueryParameter("oauth_verifier")
        val edit = getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
        edit.putString("access_token", token)
        edit.putString("access_token_verifier", verifier)
        edit.putBoolean("authorizing", false)
        edit.apply()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
