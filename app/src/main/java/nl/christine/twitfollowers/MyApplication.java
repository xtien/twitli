/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers;

import android.app.Application;
import nl.christine.twitfollowers.dagger.AppComponent;
import nl.christine.twitfollowers.dagger.DaggerAppComponent;

public class MyApplication extends Application {

    private static Application instance;

    public MyApplication(){
        instance = this;
    }

    public AppComponent appComponent = DaggerAppComponent.builder()
            .application(this)
            .build();

    public static Application getApplication() {
        return instance;
    }
}
