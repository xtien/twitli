package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.api.MyHttp
import com.twitli.android.twitter.bot.wiki.api.MyHttpImpl
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApiImpl
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.wiki.WikiPageManager
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
open class TestModule {

    lateinit var twitManager: TwitManager
    lateinit var wikiPageManager: WikiPageManager

    @Provides
    fun provideTwitManager(): TwitManager {
        if (!this::twitManager.isInitialized) run {
            twitManager = Mockito.mock(TwitManager::class.java)
        }
        return twitManager
    }
}
