package com.twitli.android.twitter.dagger

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.wiki.DictionaryRepository
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
import com.twitli.android.twitter.ui.SettingsViewModel
import com.twitli.android.twitter.wiki.WikiPageManager
import com.twitli.android.twitter.wiki.impl.WikiPageManagerImpl
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
open class ApiModule {

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
    open fun wiktionaryApi(application: Application, myHttp: MyHttp): WiktionaryApi {
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

    @Provides
    fun wikiPageManager(application: Application, contentRepository: ContentRepository): WikiPageManager {
        return WikiPageManagerImpl(application, contentRepository)
    }

    @Provides
    fun app() :Application {
        return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
    }

}
