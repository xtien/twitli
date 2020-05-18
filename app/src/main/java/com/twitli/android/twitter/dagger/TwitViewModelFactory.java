package com.twitli.android.twitter.dagger;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.tweet.TwitViewModel;

import javax.inject.Singleton;

@Singleton
public
class TwitViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final TwitManager twitManager;

    public TwitViewModelFactory(@NonNull Application application, @NonNull TwitManager twitManager) {
        this.application = application;
        this.twitManager = twitManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TwitViewModel.class)) {
            return (T) new TwitViewModel(application, twitManager);
        } else {
            return null;
        }
    }
}
