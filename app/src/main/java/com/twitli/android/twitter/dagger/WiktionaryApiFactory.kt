package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.bot.wiki.api.WiktionaryApi
import com.twitli.android.twitter.bot.wiki.api.WiktionaryApiImpl

class WiktionaryApiFactory {

    companion object {
        fun get(): WiktionaryApi {
            if(!this::instance.isInitialized ){
                instance = WiktionaryApiImpl()
            }
            return instance
        }

        lateinit  var instance : WiktionaryApi
    }

}
