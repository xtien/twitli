/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.dagger

import android.app.Application
import com.twitli.android.twitter.service.TwitService
import com.twitli.android.twitter.tweet.TwitFragment
import com.twitli.android.twitter.tweet.impl.TwitManagerImpl
import com.twitli.android.twitter.ui.MainActivity
import com.twitli.android.twitter.ui.SettingsFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(settingsFragment: SettingsFragment)
    fun inject(twitFragment: TwitFragment)
    fun inject(twitService: TwitService)
    fun inject(mainActivity: MainActivity)
    fun inject(twitManager: TwitManagerImpl)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
