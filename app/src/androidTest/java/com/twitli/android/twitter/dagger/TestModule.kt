package com.twitli.android.twitter.dagger

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
    fun provideWikiPageManager(): WikiPageManager {
        if (!this::wikiPageManager.isInitialized) {
            wikiPageManager = Mockito.mock(WikiPageManager::class.java)
        }
        return wikiPageManager
    }

    @Provides
    fun provideTwitManager(): TwitManager {
        if (!this::twitManager.isInitialized) run {
            twitManager = Mockito.mock(TwitManager::class.java)
        }
        return twitManager
    }
}
