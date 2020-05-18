/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.tweet;

import com.twitli.android.twitter.data.Content;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.RequestToken;

import java.util.List;

public interface TwitManager {

    void tweet(Content content);

    void reply(String string, Long replyToId);

    void tweet(String toString);

    void createAccessToken(String verifier);

    RequestToken createRequestToken() throws TwitterException;

    User verifyCredentials() throws TwitterException;

    void like(Long tweetId);

    List<Status> getHomeTimeline(Paging paging) throws TwitterException;
}
