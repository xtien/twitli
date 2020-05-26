/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter

import androidx.test.core.app.ApplicationProvider
import com.twitli.android.twitter.data.ContentRepository
import com.twitli.android.twitter.wiki.WikiPageManager
import com.twitli.android.twitter.wiki.impl.WikiPageManagerImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException


class TestGetPage {

    var contentRepository: ContentRepository? = null

    @Before
    fun createDb() {
        contentRepository = ContentRepository(ApplicationProvider.getApplicationContext())
    }

    @get:Throws(IOException::class)
    @get:Test
    val page: Unit
        get() {
            val wikiPageManager: WikiPageManager = WikiPageManagerImpl()
            wikiPageManager.getPage("1")
            var content = contentRepository!!.getFirst("1")
            Assert.assertNotNull(content)
            Assert.assertTrue(content!!.text!!.contains("Aarde"))
            contentRepository!!.setDone(content.id)
            content = contentRepository!!.getFirst("1")
            Assert.assertNotNull(content)
            Assert.assertTrue(content!!.text!!.contains("China"))
        }
}
