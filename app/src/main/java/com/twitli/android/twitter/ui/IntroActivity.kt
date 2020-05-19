/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.twitli.android.twitter.R

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        if (prefs.getInt("inits", 0) > 2) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_intro)
            findViewById<View>(R.id.submit).setOnClickListener { v: View? ->
                val editor = prefs.edit()
                editor.putInt("inits", prefs.getInt("inits", 0) + 1)
                editor.apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
