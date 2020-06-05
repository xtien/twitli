/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.wiki.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.twitli.android.twitter.bot.wiki.type.Word

@Entity(tableName = "adjective", indices = [Index(value = ["positive"], unique = true)])
class Adjective : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "superlative")
    var superlative: String = ""

    @ColumnInfo(name = "comparative")
    var comparative: String = ""

    @ColumnInfo(name = "positive")
    var positive: String = ""

    fun Adjective(string: String) {
        positive = string
        wordString = string
    }

    fun getString(): String? {
        return positive
    }
}
