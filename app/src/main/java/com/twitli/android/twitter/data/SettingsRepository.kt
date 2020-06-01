/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import android.app.Application
import androidx.lifecycle.LiveData
import javax.inject.Inject

class SettingsRepository @Inject constructor(application: Application?) {

    val isActive: LiveData<Boolean>
    private val settingsDao: SettingsDao

    fun setActive(isChecked: Boolean) {
        AppDatabase.databaseWriteExecutor.execute { settingsDao.setActive(isChecked) }
    }

    init {
        val db = AppDatabase.getDatabase(application!!)
        settingsDao = db!!.settingsDao()
        isActive = settingsDao.isActive()
    }
}
