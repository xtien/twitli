package com.twitli.android.twitter.dagger;

import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.wiki.WikiPageManager;
import dagger.Module;
import dagger.Provides;
import org.mockito.Mockito;

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
