package com.twitli.android.twitter.bot.wiki.api

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by christine on 9-6-15.
 */
class MyHttpImpl : MyHttp {

    override fun getWikiStringFromUrl(urlString: String): HttpResultWrapper {

        val url = URL(urlString)
        var result: HttpResultWrapper
        var string = ""
        var urlConnection: HttpsURLConnection? = url.openConnection() as HttpsURLConnection
        try {
            urlConnection!!.connectTimeout = 5000
            urlConnection.instanceFollowRedirects = true
            val `is` = urlConnection.inputStream
            val `in` = InputStreamReader(`is`)
            val br = BufferedReader(`in`, 512)
            var line: String
            while (br.readLine().also { line = it } != null) {
                string = string + line
            }
            br.close()
            `in`.close()
            `is`.close()
            val msg = urlConnection.responseMessage
            val httpResult = urlConnection.responseCode
            if (httpResult != HttpURLConnection.HTTP_OK) {
                Log.e(LOGTAG, msg)
            }
            result = HttpResultWrapper(httpResult, msg, string)
        } finally {
            urlConnection!!.disconnect()
            urlConnection = null
        }
        return result
    }

    companion object {
        private val LOGTAG = MyHttpImpl::class.java.simpleName
        private const val readTimeout = 10000
        private const val connectTimeout = 15000
    }
}
