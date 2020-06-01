package com.twitli.android.twitter.bot.wiki.api

import android.app.Application
import android.content.Context
import android.util.Log
import com.twitli.android.twitter.MyApplication
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class WiktionaryApiImpl @Inject constructor(application: Application, myHttp: MyHttp): WiktionaryApi {

    private val LOGTAG = WiktionaryApiImpl::class.qualifiedName
    private val httpString: String = "https://nl.wiktionary.org/wiki/"

    var context: Context = application

    private var client: MyHttp = myHttp

    init {
        (context!!.applicationContext as MyApplication).appComponent!!.inject(this)
    }

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
