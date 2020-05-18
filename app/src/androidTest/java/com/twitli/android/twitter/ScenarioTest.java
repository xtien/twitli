package com.twitli.android.twitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.when;

public class ScenarioTest {

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
        edit.apply();

        activityRule.launchActivity(new Intent());
        TestComponent appComponent = (TestComponent)((MyApplication) activityRule.getActivity().getApplication()).appComponent;
        appComponent.inject(this);
        when(twitManager.verifyCredentials()).thenReturn(user);
    }

    @Test
    public void clickingButton_shouldChangeMessage() {

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.tweet_button)).check(matches(isDisplayed()));
        onView(withId(R.id.tweet_button)).perform(click());
        onView(withId(R.id.tweet)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.status_text)).perform(new ViewAction[]{ViewActions.typeText("name")});
        scenario.recreate();
        onView(ViewMatchers.withId(R.id.status_text)).check(ViewAssertions.matches(ViewMatchers.withText("name")));
    }
}
