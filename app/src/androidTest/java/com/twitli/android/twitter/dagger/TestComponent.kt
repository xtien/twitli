/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.ScenarioTest
import com.twitli.android.twitter.TestSwipe
import com.twitli.android.twitter.TestTweet
import com.twitli.android.twitter.ui.MainActivity
import dagger.Component

@Component(modules = [TestModule::class])
interface TestComponent : AppComponent {

    fun inject(testTweet: TestTweet?)
    fun inject(testSwipe: TestSwipe?)
    fun inject(scenarioTest: ScenarioTest?)
    override fun inject(mainActivity: MainActivity)
}
