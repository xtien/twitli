package com.twitli.android.twitter.test;

import com.twitli.android.twitter.dagger.AppModule;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.wiki.WikiPageManager;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import org.mockito.Mockito;

import javax.inject.Singleton;

@Module
public class TestModule {

    private TwitManager twitManager;

    @Provides
    public WikiPageManager provideWikiPageManager() {
        return Mockito.mock(WikiPageManager.class);
    }

    @Provides
    public TwitManager provideTwitManager() {
        if(twitManager == null){
            twitManager = Mockito.mock(TwitManager.class);
        }
        return twitManager;
    }

}
