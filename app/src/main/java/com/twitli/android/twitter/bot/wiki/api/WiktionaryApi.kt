package com.twitli.android.twitter.bot.wiki.api

interface WiktionaryApi {

    @Override
    fun getWikiWord(string: String): String?
}
