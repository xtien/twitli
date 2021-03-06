package com.twitli.android.twitter.tweet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "twit_table")
class Tweet {

    @ColumnInfo(name = "tweet_id")
    @PrimaryKey
    var tweetId: Long = 0L

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "screen_name")
    var screenName: String? = null

    @ColumnInfo(name = "time")
    var time: Long = 0L

    @ColumnInfo(name = "text")
    var text: String? = null

    @ColumnInfo(name = "liked")
    var isLiked = false

    @ColumnInfo(name = "number_of_likes")
    var likes = 0

    @ColumnInfo(name = "processed")
    var processed = false
}
