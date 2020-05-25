package com.twitli.android.twitter.dagger

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitViewModel
import javax.inject.Singleton

@Singleton
class TwitViewModelFactory(private val application: Application, private val twitManager: TwitManager, private val chatbot: ChatBot) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return (if (modelClass.isAssignableFrom(TwitViewModel::class.java)) {
            TwitViewModel(application, twitManager, chatbot) as T
        } else {
            null
        }) as T
    }
}
