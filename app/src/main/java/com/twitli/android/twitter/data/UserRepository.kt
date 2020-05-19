/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import android.app.Application
import androidx.lifecycle.LiveData
import twitter4j.User

class UserRepository(application: Application?) {
    private val userDao: UserDao
    val followersCount: LiveData<Long?>?

    fun persist(user: User) {
        userDao.persist(user.id, user.followersCount, user.friendsCount, user.name, user.location, user.screenName)
    }

    fun setFollowers(followersCount: Int) {
        userDao.setFollowers(followersCount)
    }

    val year: Int
        get() = userDao.year

    init {
        val db = AppDatabase.getDatabase(application!!)
        userDao = db!!.userDao()
        followersCount = userDao.followersCount
    }
}
