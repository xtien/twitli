package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.wiki.impl.WiktionaryBotImpl

class WiktionaryBotFactory {

    companion object {
        fun get(): WiktionaryBot {
            if(!this::instance.isInitialized ){
                instance = WiktionaryBotImpl()
            }
            return instance
        }

        lateinit  var instance : WiktionaryBot
    }
}
