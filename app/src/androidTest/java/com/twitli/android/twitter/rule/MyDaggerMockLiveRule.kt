/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.rule

import androidx.test.platform.app.InstrumentationRegistry
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.dagger.ApiLiveTestComponent
import it.cosenonjaviste.daggermock.DaggerMockRule

class MyDaggerMockLiveRule : DaggerMockRule<ApiLiveTestComponent>(ApiLiveTestComponent::class.java) {

    private val app: MyApplication
        get() = InstrumentationRegistry.getInstrumentation()
                .targetContext.applicationContext as MyApplication

    init {
        set { component: ApiLiveTestComponent? -> app.setComponent((component)!!) }
    }
}
