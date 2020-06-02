/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.wiki

import androidx.room.*
import com.twitli.android.twitter.bot.wiki.type.Adjective
import com.twitli.android.twitter.bot.wiki.type.Adverb
import com.twitli.android.twitter.bot.wiki.type.Noun
import com.twitli.android.twitter.bot.wiki.type.Verb

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVerb(noun: Verb): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateVerb(noun: Verb)

    @Transaction
    fun createOrUpdate(noun: Verb) {
        val id = insertVerb(noun)
        if (id == -1L) {
            updateVerb(noun)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAdjective(noun: Adjective): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAdjective(noun: Adjective)

    @Transaction
    fun createOrUpdate(noun: Adjective) {
        val id = insertAdjective(noun)
        if (id == -1L) {
            updateAdjective(noun)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(noun: Adverb): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(noun: Adverb)

    @Transaction
    fun createOrUpdate(noun: Adverb) {
        val id = insert(noun)
        if (id == -1L) {
            update(noun)
        }
    }
}
