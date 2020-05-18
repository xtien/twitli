package com.twitli.android.twitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.twitli.android.twitter.dagger.AppComponent;
import com.twitli.android.twitter.dagger.AppModule;
import com.twitli.android.twitter.test.MyDaggerMockRule;
import com.twitli.android.twitter.test.TestComponent;
import com.twitli.android.twitter.test.TestModule;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.ui.MainActivity;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import twitter4j.TwitterException;
import twitter4j.User;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.when;

public class TestTweet {

    TwitManager twitter;
    User user;

    @Rule
    public DaggerMockRule<TestComponent> daggerRule = new MyDaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setup() {
        SharedPreferences prefs = getInstrumentation().getTargetContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("access_token", "123");
        edit.putString("access_token_secret", "123");
        edit.commit();

    }

    @Test
    public void tweetbutton() throws TwitterException {

        when(twitter.verifyCredentials()).thenReturn(user);

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.tweet)).check(matches(isDisplayed()));
    }
}
