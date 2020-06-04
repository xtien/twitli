/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import android.content.Context
import android.util.Log
import javax.inject.Inject

class ContentRepository @Inject constructor(context: Context) {
    private val contentDao: ContentDao?

    fun addContent(year: String?, datum: String?, text: String?) {

        var existingContent: Content = if (datum != null) {
            getContent(year, datum, text)
        } else {
            getContentNoDate(year, text)
        }

        if (existingContent == null) {
            contentDao!!.addContent(year, datum, text)
        }
    }

    private fun getContent(year: String?, datum: String?, text: String?): Content {
        return contentDao!!.getContentForDate(year, datum, text)
    }

    private fun getContentNoDate(year: String?, text: String?): Content {
        return contentDao!!.getContentNoDate(year, text)
    }

    fun getFirstUnused(year: String?): Content? {
        val list = contentDao!!.getFirstUnused(year)
        return if (list!!.isEmpty()) null else list[0]
    }

    fun getFirst(year: String?): Content? {
        val list = contentDao!!.getFirst(year)
        return if (list!!.isEmpty()) null else list[0]
    }

    fun getAll(year: String?): List<Content?>? {
        return contentDao!!.getAll(year)
    }

    fun setDone(id: Int) {
        contentDao!!.setDone(id)
    }

    fun getStatus(year: Int): ContentStatus {
        val available = contentDao!!.getAvailable(year)
        for (content in available!!) {
            Log.d(LOGTAG, content!!.year + " " + content.date + " " + content.text)
        }
        val done = contentDao.getDone(year)
        return if (available.size > 0) {
            ContentStatus.AVAILABLE
        } else {
            if (done!!.size > 0) {
                ContentStatus.DONE
            } else {
                ContentStatus.NONE
            }
        }
    }

    fun getAvailable(year: Int): List<Content?>? {
        return contentDao!!.getAvailable(year)
    }

    fun clear() {
        contentDao!!.clear()
    }

    companion object {
        private val LOGTAG = ContentRepository::class.java.simpleName
    }

    init {
        val db = AppDatabase.getDatabase(context!!)
        contentDao = db!!.contentDao()
    }
}
