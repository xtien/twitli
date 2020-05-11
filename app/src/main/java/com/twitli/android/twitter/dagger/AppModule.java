/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.dagger;

import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.wiki.WikiPageManager;
import com.twitli.android.twitter.wiki.WikiPageManagerImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    public WikiPageManager provideWikiPageManager() {
        return new WikiPageManagerImpl();
    }

    @Provides
    public TwitManager provideTwitManager() {
        return TwitManagerFactory.getInstance();
    }
}
