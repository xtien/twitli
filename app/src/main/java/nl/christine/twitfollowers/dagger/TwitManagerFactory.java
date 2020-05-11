/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.dagger;

import nl.christine.twitfollowers.tweet.TwitManager;
import nl.christine.twitfollowers.tweet.TwitManagerImpl;

public class TwitManagerFactory {

    private static TwitManager instance;

    public static TwitManager getInstance(){
        if(instance == null){
            instance = new TwitManagerImpl();
        }
        return instance;
    }
}
