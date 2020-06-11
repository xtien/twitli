package com.twitli.android.twitter.bot.wiki.api

import android.app.Application
import android.content.Context
import android.util.Log
import com.twitli.android.twitter.R
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class WiktionaryApiImpl @Inject constructor(application: Application, myHttp: MyHttp): WiktionaryApi {

    private val LOGTAG = WiktionaryApiImpl::class.qualifiedName
    private var httpString: String = ""

    var context: Context = application

    private var client: MyHttp = myHttp

    init {
        httpString = context.getString(R.string.wiktionary_http_string)
    }

    override fun getWikiWord(string: String): String? {

        val url: String = httpString + string
        var httpResult: HttpResultWrapper? = null
        try {
            httpResult = client.getWikiStringFromUrl(url)
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
