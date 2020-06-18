package com.twitli.android.twitter.tweet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
    fun getTweetLiveData(id: Long): LiveData<Tweet?>?

    @Query("select * from twit_table where tweet_id = :id")
    fun getTweet(id: Long):Tweet?

    @Query("replace into twit_table ('tweet_id','name', 'screen_name', 'time', 'text', 'liked', 'number_of_likes', 'processed') values (:id, :name, :screenName, :time, :text, :liked, :numberOfLikes, :processed)")
    fun store(id: Long, name: String?, screenName: String?, time: Long, text: String?, liked: Boolean, numberOfLikes: Int, processed: Boolean)

    @Query("delete from twit_table where time < :timestamp")
    fun cleanUp(timestamp: Long)

    @Query("select * from twit_table where processed = 0")
    fun getNewTweet(): Tweet

    @Query("update twit_table set processed = 1 WHERE tweet_id = :tweetId")
    fun setTweetDone(tweetId: Long)

    @Query("update twit_table set liked = :liked WHERE tweet_id = :tweetId")
    abstract fun onLikeClicked(tweetId: Long, liked: Boolean)

    @Query("delete from twit_table where tweet_id = :tweetId")
    fun delete(tweetId: Long)
}
