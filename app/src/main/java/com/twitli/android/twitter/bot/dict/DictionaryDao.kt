/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

import androidx.room.*
import com.twitli.android.twitter.bot.dict.type.*

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(noun: Noun): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(noun: Noun)

    @Transaction
    fun createOrUpdate(noun: Noun) {
        noun.timeStamp = System.currentTimeMillis()
        noun.timesUsed++
        val id = insert(noun)
        if (id == -1L) {
            update(noun)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVerb(verb: Verb): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateVerb(verb: Verb)

    @Transaction
    fun createOrUpdate(verb: Verb) {
        verb.timeStamp = System.currentTimeMillis()
        verb.timesUsed++
        val id = insertVerb(verb)
        if (id == -1L) {
            updateVerb(verb)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAdjective(adjective: Adjective): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAdjective(adjective: Adjective)

    @Transaction
    fun createOrUpdate(adjective: Adjective) {
        adjective.timeStamp = System.currentTimeMillis()
        adjective.timesUsed++
        val id = insertAdjective(adjective)
        if (id == -1L) {
            updateAdjective(adjective)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(adverb: Adverb): Long;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(adverb: Adverb)

    @Transaction
    fun createOrUpdate(adverb: Adverb) {
        adverb.timeStamp = System.currentTimeMillis()
        adverb.timesUsed++
        val id = insert(adverb)
        if (id == -1L) {
            update(adverb)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createOrUpdate(word: WordString)

    @Query("select * from noun WHERE wordString = :string")
    fun getNoun(string: String) : List<Noun>

    @Query("select * from verb WHERE wordString = :string")
    fun getVerb(string: String): List<Verb>

    @Query("select * from adjective WHERE wordString = :string")
    fun getAdjective(string: String): List<Adjective>

    @Query("delete from noun")
    fun clearNouns()

    @Query("delete from verb")
    fun clearVerbs()

    @Query("delete from adjective")
    fun clearAdjectives()

    @Insert
    fun createOrUpdate(personalPronoun: PersonalPronoun)

    @Insert
    fun insertAll(populateData: Array<PersonalPronoun>)

    @Query("select * from personalpronoun WHERE wordString = :string")
    fun getPersonalPronoun(string: String): List<PersonalPronoun>

    @Query("select * from personalpronoun")
    fun getPersonalPronouns()  : List<PersonalPronoun>

    @Query("select * from noun")
    fun getNouns(): List<Noun>

    @Transaction
    fun createOrUpdate(number: MyNumber) {
        number.timeStamp = System.currentTimeMillis()
        number.timesUsed++
        val id = insert(number)
        if (id == -1L) {
            update(number)
        }
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(number: MyNumber)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(number: MyNumber): Long
}
