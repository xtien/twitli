/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.dagger;

import android.app.Application;
import com.twitli.android.twitter.service.TwitService;
import com.twitli.android.twitter.tweet.TwitFragment;
import com.twitli.android.twitter.ui.MainActivity;
import com.twitli.android.twitter.ui.MainFragment;
import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainFragment mainFragment);
    void inject(TwitFragment twitFragment);
    void inject(TwitService twitService);
    void inject(MainActivity mainActivity);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}
