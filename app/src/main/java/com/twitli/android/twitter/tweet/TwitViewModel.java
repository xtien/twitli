package com.twitli.android.twitter.tweet;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TwitViewModel extends AndroidViewModel {

    private final TwitRepository repository;
    private final LiveData<List<Tweet>> tweets;

    public TwitViewModel(Application application) {
        super(application);
        repository = new TwitRepository(application);
        tweets = repository.getTweets();
    }

    public LiveData<List<Tweet>> getTweets() {
        return tweets;
    }

    public void clear() {
        repository.clear();
    }

    public void loadTweets() {
        repository.loadTweets();
    }
}
