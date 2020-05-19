/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SettingsDao {
    @Insert
    fun insert(settings: MySettings?)

    @Query("select active from settings_table where settingsid = 0")
    fun isActive(): LiveData<Boolean?>?

    @Query("update settings_table set active = :active where settingsid =0")
    fun setActive(active: Boolean)
}
