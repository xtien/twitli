/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import nl.christine.twitfollowers.data.MySettings;
import nl.christine.twitfollowers.data.SettingsRepository;

import javax.inject.Inject;

public class SettingsViewModel extends AndroidViewModel {

    private LiveData<Boolean> active;
    private SettingsRepository repository;

    @Inject
    public SettingsViewModel(Application application) {
        super(application);
        repository = new SettingsRepository(application);
        active = repository.isActive();
    }

    public void setActive(boolean isChecked) {
        repository.setActive(isChecked);
    }
}
