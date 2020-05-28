package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.bot.wiki.api.MyHttp
import com.twitli.android.twitter.bot.wiki.api.MyHttpImpl
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
open class ApiTestModule {

    @Provides
    open fun wiktionaryApi(): WiktionaryApi {
        return Mockito.mock(WiktionaryApi::class.java)
    }

    @Provides
    fun myHttp() : MyHttp {
        return Mockito.mock(MyHttpImpl::class.java)
    }
}
