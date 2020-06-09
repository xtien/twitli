/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

import androidx.room.TypeConverter
import com.twitli.android.twitter.bot.dict.type.Gender

class Converters {

    @TypeConverter
    fun fromGender(value: Gender?): String? {
        return when(value){
            null -> null
            else -> value.name
        }
    }

    @TypeConverter
    fun toGender(value: String?): Gender? {
        return when(value){
            null -> null
            else -> Gender.valueOf(value)
        }
    }
}
