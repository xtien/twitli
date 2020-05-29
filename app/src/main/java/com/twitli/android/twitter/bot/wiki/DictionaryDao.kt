/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.wiki

import androidx.room.*
import com.twitli.android.twitter.bot.wiki.type.Noun

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(noun: Noun): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(noun: Noun)

    @Transaction
    fun createOrUpdate(noun: Noun) {
        val id = insert(noun)
        if (id == -1L) {
            update(noun)
        }
    }
}
