/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.dagger;

import androidx.test.platform.app.InstrumentationRegistry;
import com.twitli.android.twitter.MyApplication;
import it.cosenonjaviste.daggermock.DaggerMockRule;

public class MyDaggerMockRule extends DaggerMockRule<TestComponent> {

    public MyDaggerMockRule() {
        super(TestComponent.class, new TestModule());
        set(component -> MyDaggerMockRule.this.getApp().setComponent(component));
    }

    private MyApplication getApp() {
        return (MyApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }
}
