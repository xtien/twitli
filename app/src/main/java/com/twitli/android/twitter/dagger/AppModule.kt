/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.dagger

import android.app.Application
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.dict.DictionaryRepository
import com.twitli.android.twitter.bot.impl.ChatBotImpl
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.api.MyHttp
import com.twitli.android.twitter.bot.wiki.api.MyHttpImpl
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApiImpl
import com.twitli.android.twitter.bot.wiki.impl.WiktionaryBotImpl
import com.twitli.android.twitter.data.ContentRepository
import com.twitli.android.twitter.data.SettingsRepository
import com.twitli.android.twitter.data.UserRepository
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitRepository
import com.twitli.android.twitter.tweet.TwitViewModel
import com.twitli.android.twitter.tweet.impl.TwitManagerImpl
import com.twitli.android.twitter.ui.SettingsViewModel
import com.twitli.android.twitter.wiki.WikiPageManager
import com.twitli.android.twitter.wiki.impl.WikiPageManagerImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule() {

    lateinit var twitManager: TwitManager
    lateinit var chatBot: ChatBot

    @Provides
    fun twitViewModel(application: Application, twitManager: TwitManager): TwitViewModel {
        return TwitViewModel(application, twitManager)
    }

    @Provides
    fun settingsViewModel(application: Application, settingsRepository: SettingsRepository): SettingsViewModel {
        return SettingsViewModel(application, settingsRepository)
    }

    @Provides
    fun dictionaryRepository(application: Application): DictionaryRepository {
        return DictionaryRepository(application)
    }

    @Provides
    fun settingsRepository(application: Application): SettingsRepository {
        return SettingsRepository(application)
    }

    @Provides
    fun twitRepository(application: Application?, twitManager: TwitManager): TwitRepository {
        return TwitRepository(application, twitManager)
    }

    @Provides
    fun userRepository(application: Application): UserRepository {
        return UserRepository(application)
    }

    @Provides
    fun contentRepository(application: Application): ContentRepository {
        return ContentRepository(application)
    }

    @Provides
    fun wikiPageManager(application: Application, contentRepository: ContentRepository): WikiPageManager {
        return WikiPageManagerImpl(application, contentRepository)
    }

    @Provides
    fun provideTwitManager(application: Application): TwitManager {
        if (!this::twitManager.isInitialized) {
            twitManager = TwitManagerImpl(application)
        }
        return twitManager
    }

    @Provides
    fun chatbot(application: Application, wikBot: WiktionaryBot, twit: TwitManager, twitRepository: TwitRepository): ChatBot {
        if(!this::chatBot.isInitialized){
            chatBot = ChatBotImpl(application, wikBot, twit, twitRepository)
        }
        return chatBot
    }

    @Provides
    fun wiktionaryApi(application: Application, myHttp: MyHttp): WiktionaryApi {
        return WiktionaryApiImpl(application, myHttp)
    }

    @Provides
    fun wiktionaryBot(application: Application, dictionaryRepository: DictionaryRepository, wiktionaryApi: WiktionaryApi): WiktionaryBot {
        return WiktionaryBotImpl(application, dictionaryRepository, wiktionaryApi)
    }

    @Provides
    fun myHttp(): MyHttp {
        return MyHttpImpl()
    }
}
