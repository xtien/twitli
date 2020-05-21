package com.twitli.android.twitter.dagger

import com.twitli.android.twitter.wiki.WikiPageManager
import com.twitli.android.twitter.wiki.WikiPageManagerImpl

class WikiPageManagerFactory {

    companion object {
        fun get(): WikiPageManager {
            if(!this::instance.isInitialized){
                instance = WikiPageManagerImpl()
            }
            return instance
        }

        lateinit var instance : WikiPageManager
    }

}
