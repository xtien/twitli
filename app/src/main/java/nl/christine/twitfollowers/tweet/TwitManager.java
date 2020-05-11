/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.tweet;

import nl.christine.twitfollowers.data.Content;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import javax.inject.Singleton;

public interface TwitManager {

    void tweet(Content content);

    void tweet(String toString);

    void createAccessToken(String verifier);

    RequestToken createRequestToken() throws TwitterException;
}