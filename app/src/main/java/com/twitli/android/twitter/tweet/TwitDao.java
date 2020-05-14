package com.twitli.android.twitter.tweet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TwitDao {

    @Query("select * from twit_table ORDER BY tweet_id DESC LIMIT 20")
    public LiveData<List<Tweet>> getTweets();

    @Insert
    void create(Tweet tweet);

    @Query("delete from twit_table")
    void clear();

    @Query("select * from twit_table where tweet_id = :id")
    LiveData<Tweet> getTweet(int id);

    @Query("replace into twit_table ('tweet_id','name', 'screen_name', 'time', 'text') values (:id, :name, :screenName, :time, :text)")
    void store(long id, String name, String screenName, long time, String text);

    @Query("delete from twit_table where time < :timestamp")
    void cleanUp(long timestamp);
}
