package com.twitli.android.twitter.test;

import com.twitli.android.twitter.TestTweet;
import com.twitli.android.twitter.dagger.AppComponent;
import com.twitli.android.twitter.ui.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {TestModule.class})
public interface TestComponent extends AppComponent{

    void inject(TestTweet testTweet);
    void inject(MainActivity mainActivity);

}
