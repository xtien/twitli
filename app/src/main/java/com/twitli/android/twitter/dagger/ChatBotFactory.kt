package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.impl.ChatBotImpl
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.impl.TwitManagerImpl

class ChatBotFactory {

    companion object {

        fun get(): ChatBot {
            if(!this::instance.isInitialized ){
                instance = ChatBotImpl()
            }
            return instance
        }

        lateinit  var instance : ChatBot
    }
}
