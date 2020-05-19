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
interface UserDao {
    @get:Query("select followers_count from users_table where me = 1")
    val followersCount: LiveData<Long?>?

    @Query("update users_table set id = :id, followers_count = :followersCount, friends_count = :friendsCount, name = :name, location = :location, screen_name = :screenName where me = 1")
    fun persist(id: Long, followersCount: Int, friendsCount: Int, name: String?, location: String?, screenName: String?)

    @Query("update users_table set followers_count = :followersCount where me = 1")
    fun setFollowers(followersCount: Int)

    @get:Query("select followers_count from users_table where me = 1")
    val year: Int

    @Insert
    fun insert(user: User?)
}
