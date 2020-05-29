/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.dagger

import android.app.Application
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.wiki.DictionaryRepository
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.api.MyHttp
import com.twitli.android.twitter.bot.wiki.api.MyHttpImpl
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.wiki.WikiPageManager
import dagger.Module
import dagger.Provides

@Module
class AppModule() {

    @Provides
    fun dictionaryRepository(application: Application): DictionaryRepository {
        return DictionaryRepository(application)
    }

    @Provides
    fun wikiPageManager(): WikiPageManager {
        return WikiPageManagerFactory.get()
    }

    @Provides
    fun twitManager(): TwitManager {
        return TwitManagerFactory.get()
    }

    @Provides
    fun chatbot(): ChatBot {
        return ChatBotFactory.get();
    }

    @Provides
    fun wiktionaryApi(): WiktionaryApi {
        return WiktionaryApiFactory.get()
    }

    @Provides
    fun wiktionaryBot(): WiktionaryBot {
        return WiktionaryBotFactory.get()
    }

    @Provides
    fun myHttp(): MyHttp {
        return MyHttpImpl()
    }
}
