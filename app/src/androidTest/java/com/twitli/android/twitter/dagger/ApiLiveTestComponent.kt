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

@Component(modules = [TestModule::class, ApiModule::class])
interface ApiLiveTestComponent : TestComponent {

    fun inject(classifyTest: WiktionaryClassifyLiveTest)
    fun inject(pageTest: WiktionaryPageLiveTest)
    fun inject(pageTest: TestGetNLPage)
    fun inject(pageTest: TestGetPage)
    fun inject(test: WiktionarySentenceLiveTest)
    fun inject(patternTest: PatternTest)
    fun inject(analysisTest: AnalysisTest)
    fun inject(patternTest: PatternFormatTest)

    override fun inject(mainActivity: MainActivity)
}
