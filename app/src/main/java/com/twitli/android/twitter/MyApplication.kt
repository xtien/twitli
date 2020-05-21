/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.twitli.android.twitter.dagger.AppComponent
import com.twitli.android.twitter.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MyApplication : Application(), HasAndroidInjector {

    companion object{
        lateinit var instance: MyApplication
    }

     lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()

        instance = this;

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    @VisibleForTesting
    fun setComponent(component: AppComponent) {
        //  appComponent = component
    }
}
