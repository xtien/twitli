package com.twitli.android.twitter.bot

import twitter4j.Status

interface ChatBot {

    fun read(tweets: List<Status?>?)
}
