package com.twitli.android.twitter.dagger

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.dict.DictionaryRepository
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.api.MyHttp
import com.twitli.android.twitter.bot.wiki.api.MyHttpImpl
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.data.ContentRepository
import com.twitli.android.twitter.data.SettingsRepository
import com.twitli.android.twitter.data.UserRepository
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitRepository
import com.twitli.android.twitter.tweet.TwitViewModel
import com.twitli.android.twitter.ui.SettingsViewModel
import com.twitli.android.twitter.wiki.WikiPageManager
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
open class ApiTestModule {

    lateinit var wikiPageManager: WikiPageManager

    @Provides
    fun twitViewModel(application: Application, twitManager: TwitManager, chatbot: ChatBot): TwitViewModel {
        return TwitViewModel(application, twitManager, chatbot)
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
    fun twitRepository(application: Application?, twitManager: TwitManager, chatbot: ChatBot): TwitRepository {
        return TwitRepository(application, twitManager, chatbot)
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
    open fun wiktionaryApi(): WiktionaryApi {
        return Mockito.mock(WiktionaryApi::class.java)
    }

    @Provides
    fun wiktionaryBot() : WiktionaryBot {
        return Mockito.mock(WiktionaryBot::class.java)
    }

    @Provides
    fun provideWikiPageManager(): WikiPageManager {
        if (!this::wikiPageManager.isInitialized) {
            wikiPageManager = Mockito.mock(WikiPageManager::class.java)
        }
        return wikiPageManager
    }

    @Provides
    fun myHttp() : MyHttp {
        return Mockito.mock(MyHttpImpl::class.java)
    }

    @Provides
    fun app() :Application {
        return getInstrumentation().targetContext.applicationContext as Application
    }
}
