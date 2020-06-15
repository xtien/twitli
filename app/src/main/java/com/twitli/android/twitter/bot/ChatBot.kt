package com.twitli.android.twitter.bot

import com.twitli.android.twitter.bot.dict.type.Word
import com.twitli.android.twitter.tweet.Tweet

interface ChatBot {

    fun processTweet(status: Tweet)

    fun sentenceWords(words: List<Word>): List<String>
}
