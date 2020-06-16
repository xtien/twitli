package com.twitli.android.twitter.tweet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TwitViewModel(application: Application?, twitManager: TwitManager?) : AndroidViewModel(application!!) {

    private val repository: TwitRepository = TwitRepository(application, twitManager!!)
    val tweets: LiveData<List<Tweet?>?>?

    fun loadTweets() {
        repository.loadTweets()
    }

    fun onLikeClicked(tweetId: Long, liked: Boolean) {
        repository.onLikeClicked(tweetId, liked)
    }

    init {
        tweets = repository.tweets
    }
}
