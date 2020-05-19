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

@Entity(tableName = "users_table")
class User {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "twitter_id")
    var twitterId: String? = null

    @ColumnInfo(name = "me")
    var isMe = false

    @ColumnInfo(name = "name")
    var name = 0

    @ColumnInfo(name = "screen_name")
    var screenName = 0

    @ColumnInfo(name = "location")
    var location = 0

    @ColumnInfo(name = "followers_count")
    var followersCount = 0

    @ColumnInfo(name = "friends_count")
    var friendsCount = 0

}
