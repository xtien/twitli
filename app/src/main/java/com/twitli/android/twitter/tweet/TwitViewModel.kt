package com.twitli.android.twitter.tweet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TwitViewModel(application: Application?, twitManager: TwitManager?) : AndroidViewModel(application!!) {
    private val repository: TwitRepository
    val tweets: LiveData<List<Tweet?>?>?

    fun clear() {
        repository.clear()
    }

    fun loadTweets() {
        repository.loadTweets()
    }

    init {
        repository = TwitRepository(application, twitManager!!)
        tweets = repository.tweets
    }
}
