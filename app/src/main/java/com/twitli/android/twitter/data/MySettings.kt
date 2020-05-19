/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_table")
class MySettings {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "settingsid")
    var settingsId = 0

    @ColumnInfo(name = "active")
    var isActive: Boolean? = null
        private set

    @ColumnInfo(name = "followers")
    var followers = 0

    fun setActive(active: Boolean) {
        isActive = active
    }

}
