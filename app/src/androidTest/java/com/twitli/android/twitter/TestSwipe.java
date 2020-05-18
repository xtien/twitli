package com.twitli.android.twitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.test.rule.ActivityTestRule;
import com.twitli.android.twitter.dagger.MyDaggerMockRule;
import com.twitli.android.twitter.dagger.TestComponent;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.ui.MainActivity;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.*;

public class TestSwipe {

    @Inject
    TwitManager twitManager;

    User user;

    @Rule
    public DaggerMockRule<TestComponent> daggerRule = new MyDaggerMockRule();

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setup() throws TwitterException {
        SharedPreferences prefs = getInstrumentation().getTargetContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("access_token", "123");
        edit.putString("access_token_secret", "123");
        edit.commit();

        activityRule.launchActivity(new Intent());
        TestComponent appComponent = (TestComponent)((MyApplication) activityRule.getActivity().getApplication()).appComponent;
        appComponent.inject(this);
        when(twitManager.verifyCredentials()).thenReturn(user);
    }

    @Test
    public void swipe() throws TwitterException {
        onView(withId(R.id.main)).perform(swipeLeft());

        onView(withId(R.id.version)).check(matches(withText("2.0")));
    }

}