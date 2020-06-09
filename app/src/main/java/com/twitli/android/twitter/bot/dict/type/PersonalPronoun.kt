/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict.type

import androidx.room.*
import com.twitli.android.twitter.bot.dict.Converters

@Entity(tableName = "personalpronoun")
@TypeConverters(Converters::class)
class PersonalPronoun() : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "singular")
    private var person = 0

    @ColumnInfo(name = "plural")
    private var plural = false

    @ColumnInfo(name = "gender")
    private var gender: Gender = Gender.F

    @ColumnInfo(name = "declension")
    private var declension = 0

    constructor(string: String, person: Int, declension: Int, plural: Boolean, gender: Gender) : this() {
        this.wordString = string
        this.person = person
        this.declension = declension
        this.plural = plural
        this.gender = gender
        this.type = "PersonalPronoun"
    }

    constructor(string: String, person: Int, declension: Int, plural: Boolean) :
            this(string, person, declension, plural, Gender.U)

    fun getGender(): Gender {
        return gender
    }

    fun setGender(gender: Gender) {
        this.gender = gender
    }

    fun setPerson(person: Int) {
        this.person = person
    }

    fun getPerson(): Int {
        return person
    }

    fun isFirstPerson(): Boolean {
        return person == 1
    }

    fun isSecondPerson(): Boolean {
        return person == 2
    }

    fun isThirdPerson(): Boolean {
        return person == 3
    }

    fun isFirstDeclension(): Boolean {
        return declension == 1
    }

    fun setDeclension(declension: Int) {
        this.declension = declension
    }

    fun isPlural(): Boolean {
        return plural
    }

    fun setPlural(plural: Boolean) {
        this.plural = plural
    }

    fun getDeclension(): Int {
        return declension
    }
}
