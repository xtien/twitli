package com.twitli.android.twitter.bot

import com.twitli.android.twitter.bot.dict.type.Word
import twitter4j.Status

interface ChatBot {

    fun read(tweets: List<Status?>?)

    fun processStatus(status: Status): List<Word>
}
