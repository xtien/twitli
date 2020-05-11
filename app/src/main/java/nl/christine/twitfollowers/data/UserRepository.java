/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import twitter4j.User;

public class UserRepository {

    private final UserDao userDao;
    private LiveData<Long> followersCount;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        followersCount = userDao.getFollowersCount();
    }

    public LiveData<Long> getFollowersCount(){
        return followersCount;
    }

    public void persist(User user) {
        userDao.persist(user.getId(), user.getFollowersCount(),user.getFriendsCount(), user.getName(),  user.getLocation(), user.getScreenName());
    }

    public void setFollowers(int followersCount) {
        userDao.setFollowers(followersCount);
    }

    public int getYear() {
        return userDao.getYear();
    }
}
