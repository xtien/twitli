package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitManagerImpl

class TwitManagerFactory {

    companion object {
        fun get(): TwitManager {
            if(!this::instance.isInitialized ){
                instance = TwitManagerImpl()
            }
            return instance
        }

       lateinit  var instance : TwitManager
    }
 }
