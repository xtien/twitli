/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.tweet

import com.twitli.android.twitter.data.Content
import twitter4j.Paging
import twitter4j.Status
import twitter4j.TwitterException
import twitter4j.User
import twitter4j.auth.RequestToken

interface TwitManager {

    fun tweet(content: Content?)
    fun reply(string: String?, replyToId: Long?)
    fun tweet(toString: String?)
    fun createAccessToken(verifier: String?)

    @Throws(TwitterException::class)
    fun createRequestToken(): RequestToken?

    @Throws(TwitterException::class)
    fun verifyCredentials(): User?
    fun like(tweetId: Long?)

    @Throws(TwitterException::class)
    fun getHomeTimeline(paging: Paging?): List<Status?>?
    fun logout()
}
