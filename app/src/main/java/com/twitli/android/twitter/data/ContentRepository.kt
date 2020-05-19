/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import android.app.Application
import android.util.Log

class ContentRepository(application: Application?) {
    private val contentDao: ContentDao?
    fun addContent(year: String?, datum: String?, text: String?) {
        contentDao!!.addContent(year, datum, text)
    }

    fun addContent(year: String?, text: String?) {
        contentDao!!.addContent(year, text)
    }

    fun getFirst(year: String?): Content? {
        val list = contentDao!!.getFirst(year)
        return if (list!!.isEmpty()) null else list[0]
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

    companion object {
        private val LOGTAG = ContentRepository::class.java.simpleName
    }

    init {
        val db = AppDatabase.getDatabase(application!!)
        contentDao = db!!.contentDao()
    }
}
