package com.twitli.android.twitter.bot.wiki.api

import android.util.Log
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class WiktionaryApiImpl : WiktionaryApi {

    private val LOGTAG = WiktionaryApiImpl::class.qualifiedName
    private val httpString: String = "https://nl.wiktionary.org/wiki/"

    @Inject
    lateinit var client: MyHttp

    override fun getWikiWord(string: String): String? {

        val url: String = httpString + string
        var httpResult: HttpResultWrapper? = null
        try {
            httpResult = client?.getWikiStringFromUrl(url)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return if (httpResult?.getStatusCode() == 200) {
            httpResult.getString()
        } else {
            Log.i(LOGTAG, "String not found: $string")
            null
        }
    }
}
