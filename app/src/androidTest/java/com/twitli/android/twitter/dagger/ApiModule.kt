package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.bot.wiki.api.MyHttp
import com.twitli.android.twitter.bot.wiki.api.MyHttpImpl
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApiImpl
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
open class ApiModule {

    @Provides
    open fun wiktionaryApi(): WiktionaryApi {
        return WiktionaryApiImpl()
    }

    @Provides
    fun myHttp() : MyHttp {
        return MyHttpImpl()
    }
}
