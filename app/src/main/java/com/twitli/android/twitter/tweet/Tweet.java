package com.twitli.android.twitter.tweet;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "twit_table")
public class Tweet {

    @ColumnInfo(name = "tweet_id")
    @PrimaryKey
    @NonNull
    private Long tweetId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "screen_name")
    private String screenName;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "text")
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @NonNull
    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(@NonNull Long tweetId) {
        this.tweetId = tweetId;
    }
}
