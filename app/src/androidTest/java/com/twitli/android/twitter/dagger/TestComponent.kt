/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.*
import com.twitli.android.twitter.ui.MainActivity
import dagger.Component

@Component(modules = [TestModule::class, ApiTestModule::class])
interface TestComponent : AppComponent {

    fun inject(testTweet: TestTweet?)
    fun inject(testSwipe: TestSwipe?)
    fun inject(scenarioTest: ScenarioTest?)
    fun inject(nounTest: WiktionaryNounTest?)
    fun inject(pronounTest: TestPersonalPronouns)

    override fun inject(mainActivity: MainActivity)


}
