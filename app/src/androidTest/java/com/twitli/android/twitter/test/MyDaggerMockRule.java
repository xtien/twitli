package com.twitli.android.twitter.test;

import androidx.test.platform.app.InstrumentationRegistry;
import com.twitli.android.twitter.MyApplication;
import it.cosenonjaviste.daggermock.DaggerMockRule;

public class MyDaggerMockRule extends DaggerMockRule<TestComponent> {
    public MyDaggerMockRule() {
        super(TestComponent.class, new TestModule());
        set(component -> getApp().setComponent(component));
    }

    private MyApplication getApp() {
        return (MyApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }
}
