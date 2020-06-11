package com.twitli.android.twitter.bot.wiki.api

interface MyHttp {

    fun getWikiStringFromUrl(urlString: String): HttpResultWrapper
}
