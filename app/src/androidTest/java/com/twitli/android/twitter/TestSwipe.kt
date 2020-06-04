package com.twitli.android.twitter

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.twitli.android.twitter.rule.MyDaggerMockRule
import com.twitli.android.twitter.dagger.TestComponent
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.ui.MainActivity
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import twitter4j.TwitterException
import twitter4j.User
import javax.inject.Inject

class TestSwipe {

    @Inject
    lateinit  var twitManager: TwitManager

    var user: User? = null

    @Rule
    @JvmField
    var daggerRule: DaggerMockRule<TestComponent> = MyDaggerMockRule()

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    @Throws(TwitterException::class)
    fun setup() {
        val prefs = InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString("access_token", "123")
        edit.putString("access_token_secret", "123")
        edit.apply()
        activityRule.launchActivity(Intent())
        val appComponent = (activityRule.activity.application as MyApplication).appComponent as TestComponent
        appComponent.inject(this)
        Mockito.`when`(twitManager!!.verifyCredentials()).thenReturn(user)
    }

    @Test
    @Throws(TwitterException::class)
    fun swipe() {
        Espresso.onView(withId(R.id.main)).perform(ViewActions.swipeLeft())
        Espresso.onView(withId(R.id.version)).check(ViewAssertions.matches(ViewMatchers.withText("2.0")))
    }
}
