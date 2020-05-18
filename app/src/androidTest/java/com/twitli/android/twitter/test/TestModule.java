package com.twitli.android.twitter.test;

import com.twitli.android.twitter.dagger.AppModule;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.wiki.WikiPageManager;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import org.mockito.Mockito;

@Module
public class TestModule {

    @Provides
    public WikiPageManager provideWikiPageManager() {
        return Mockito.mock(WikiPageManager.class);
    }

    @Provides
    public TwitManager provideTwitManager() {
        return Mockito.mock(TwitManager.class);
    }

}
