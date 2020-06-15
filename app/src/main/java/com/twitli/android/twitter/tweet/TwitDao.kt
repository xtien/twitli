package com.twitli.android.twitter.tweet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TwitDao {
    @get:Query("select * from twit_table ORDER BY tweet_id DESC LIMIT 20")
    val tweets: LiveData<List<Tweet?>?>?

    @Insert
    fun create(tweet: Tweet?)

    @Query("delete from twit_table")
    fun clear()

    @Query("select * from twit_table where tweet_id = :id")
    fun getTweet(id: Int): LiveData<Tweet?>?

    @Query("replace into twit_table ('tweet_id','name', 'screen_name', 'time', 'text', 'liked', 'number_of_likes', 'processed') values (:id, :name, :screenName, :time, :text, :liked, :numberOfLikes, :processed)")
    fun store(id: Long, name: String?, screenName: String?, time: Long, text: String?, liked: Boolean, numberOfLikes: Int, processed: Boolean)

    @Query("replace into twit_table ('tweet_id','name', 'screen_name', 'time', 'text', 'liked', 'number_of_likes', 'processed') values (:id, :name, :screenName, :time, :text, :liked, :numberOfLikes, 0)")
    fun store(id: Long, name: String?, screenName: String?, time: Long, text: String?, liked: Boolean, numberOfLikes: Int)

    @Query("delete from twit_table where time < :timestamp")
    fun cleanUp(timestamp: Long)

    @Query("select * from twit_table where processed = 0 ORDER BY time ASC LIMIT 1")
    fun getNewTweet(): Tweet

    @Query("update twit_table set processed = 1 WHERE tweet_id = :tweetId")
    fun setTweetDone(tweetId: Long)
}
