/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContentDao {
    @Insert
    fun insert(content: Content?)

    @Query("insert into content_table (year, date, text, used) values (:year, :datum, :text, 0)")
    fun addContent(year: String?, datum: String?, text: String?)

    @Query("insert into content_table (year, text, used) values (:year,  :text, 0)")
    fun addContent(year: String?, text: String?)

    @Query("select * from content_table where NOT used and year = :year")
    fun getFirst(year: String?): List<Content?>?

    @Query("select * from content_table where NOT used and year = :year")
    fun getAvailable(year: Int): List<Content?>?

    @Query("select * from content_table where used AND year = :year")
    fun getDone(year: Int): List<Content?>?

    @Query("update content_table set used = 1 where id = :id")
    fun setDone(id: Int)
}
