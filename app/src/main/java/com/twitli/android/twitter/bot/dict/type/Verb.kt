/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.apache.commons.lang3.builder.HashCodeBuilder

@Entity(tableName = "verb", indices = [Index(value = ["presentTense", "infinitive"], unique = true)])
class Verb : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "presentTense")
    var presentTense: String? = null

    @ColumnInfo(name = "presentTenseFirstPersonSingular")
    var presentTenseFirstPersonSingular: String = ""

    @ColumnInfo(name = "presentTenseThirdPersonSingular")
    var presentTenseThirdPersonSingular: String = ""

    @ColumnInfo(name = "presentTensePlural")
    var presentTensePlural: String = ""

    @ColumnInfo(name = "presentParticiple")
    var presentParticiple: String = ""

    @ColumnInfo(name = "gerund")
    var gerund: String = ""

    @ColumnInfo(name = "infinitive")
    var infinitive: String = ""

    @ColumnInfo(name = "pastParticiple")
    var pastParticiple: String = ""

    @ColumnInfo(name = "pastTense")
    var pastTense: String = ""

    @ColumnInfo(name = "pastTensePlural")
    var pastTensePlural: String = ""

    override fun hashCode(): Int {
        return HashCodeBuilder().append(presentTense).append(37).toHashCode()
    }
}
