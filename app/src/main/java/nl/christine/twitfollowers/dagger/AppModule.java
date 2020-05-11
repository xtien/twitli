/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.dagger;

import dagger.Module;
import dagger.Provides;
import nl.christine.twitfollowers.tweet.TwitManager;
import nl.christine.twitfollowers.wiki.WikiPageManager;
import nl.christine.twitfollowers.wiki.WikiPageManagerImpl;

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
