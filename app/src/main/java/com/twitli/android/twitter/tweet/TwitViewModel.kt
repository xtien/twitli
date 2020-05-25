package com.twitli.android.twitter.tweet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.twitli.android.twitter.bot.ChatBot

class TwitViewModel(application: Application?, twitManager: TwitManager?, chatbot: ChatBot) : AndroidViewModel(application!!) {

    private val repository: TwitRepository = TwitRepository(application, twitManager!!, chatbot)
    val tweets: LiveData<List<Tweet?>?>?

    fun loadTweets() {
        repository.loadTweets()
    }

    init {
        tweets = repository.tweets
    }
}
