package com.twitli.android.twitter.dagger;

import com.twitli.android.twitter.ScenarioTest;
import com.twitli.android.twitter.TestSwipe;
import com.twitli.android.twitter.TestTweet;
import com.twitli.android.twitter.ui.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {TestModule.class})
public interface TestComponent extends AppComponent{

    void inject(TestTweet testTweet);
    void inject(TestSwipe testSwipe);
    void inject(ScenarioTest scenarioTest);
    void inject(MainActivity mainActivity);

}
