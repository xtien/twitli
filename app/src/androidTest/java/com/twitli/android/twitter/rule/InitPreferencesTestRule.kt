/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.rule

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InitPreferencesTestRule : TestRule {

    override fun apply(base: Statement,
                       description: Description?): Statement {
        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {

                val prefs = InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val edit = prefs.edit()
                edit.putString("access_token", "123")
                edit.putString("access_token_secret", "123")
                edit.putLong("last_tweets_loaded", System.currentTimeMillis())
                edit.putLong("previousShowIntro", System.currentTimeMillis())
                edit.apply()

                base.evaluate()

            }
        }
    }
}
