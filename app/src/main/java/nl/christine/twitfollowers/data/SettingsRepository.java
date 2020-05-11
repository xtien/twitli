/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

public class SettingsRepository {

    private final LiveData<Boolean> active;
    private SettingsDao settingsDao;

    public SettingsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        settingsDao = db.settingsDao();
        active = settingsDao.isActive();
    }

    public LiveData<Boolean> isActive(){
        return active;
    }

    public void setActive(boolean isChecked) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            settingsDao.setActive(isChecked);
        });
    }
}
