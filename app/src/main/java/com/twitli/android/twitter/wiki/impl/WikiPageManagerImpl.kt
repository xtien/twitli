/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.wiki.impl

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.Html
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.data.ContentRepository
import com.twitli.android.twitter.wiki.WikiPageManager
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class WikiPageManagerImpl @Inject constructor(application: Application, repository: ContentRepository): WikiPageManager {

    private var gebeurtenissen = ""
    private var baseUrl = ""
    private var repository: ContentRepository = repository

    var application: Context = application

    override fun analyzePage(string: String?) {}

    init {
        baseUrl = application.getString(R.string.wikipage_base_url)
        gebeurtenissen = application.getString(R.string.gebeurtenissen)
    }

    @Throws(IOException::class)
    override fun getPage(year: String?): String? {

        var result: String? = null
        var numberOfItems = 0
        val doc = Jsoup.connect(baseUrl + year).get()
        val elements = doc.select("h2, li, ul")
        var scan = false
        for (e in elements) {
            val string = e.toString()
            if (string.contains("<h2>")) {
                scan = if (string.contains("Gebeurtenissen")) {
                    true
                } else {
                    break
                }
            }
            if (scan && string.startsWith("<li><a href=") && string.contains("title=")) {
                var text = e.text()
                val datum = string.substring(string.indexOf("title=") + 7, string.indexOf("\">"))
                if (datum.matches(Regex("[0-9]{1,2} [a-zA-Z]{3,12}"))) {
                    if (text.contains(" - ")) {
                        text = text.substring(text.indexOf(" - ") + 3)
                    }
                    text = fromHtml(if (text.length < 280) text else text.substring(0, 280))
                    repository.addContent(year, datum, text)
                    if (result == null) {
                        result = "$year, $datum: $text"
                    }
                    numberOfItems++
                }
            }
        }
        if (numberOfItems == 0) {
            scan = false
            for (e in elements) {
                val string = e.toString()
                if (scan && string.contains("<h2>")) {
                    break
                }
                if (string.contains("<h2>") && string.contains(gebeurtenissen)) {
                    scan = true
                }
                if (scan && string.startsWith("<li><a href=") && string.contains("title=")) {
                    var text = e.text()
                    text = fromHtml(if (text.length < 280) text else text.substring(0, 280))
                    repository.addContent(year, null, text)
                    if (result == null) {
                        result = "$year: $text"
                    }
                }
            }
        }
        return result
    }

    companion object {
        private val LOGTAG = WikiPageManagerImpl::class.java.simpleName
        fun fromHtml(html: String?): String {
            return if (html == null) {
                ""
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(html).toString()
            }
        }
    }
}
