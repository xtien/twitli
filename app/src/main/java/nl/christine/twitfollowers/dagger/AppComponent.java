/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.dagger;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import nl.christine.twitfollowers.service.TwitService;
import nl.christine.twitfollowers.ui.MainActivity;
import nl.christine.twitfollowers.ui.MainFragment;

@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainFragment mainFragment);

    void inject(TwitService twitService);

    void inject(MainActivity mainActivity);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}
