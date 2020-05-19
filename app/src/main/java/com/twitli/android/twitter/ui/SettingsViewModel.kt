/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.twitli.android.twitter.data.SettingsRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(application: Application?) : AndroidViewModel(application!!) {
    private val active: LiveData<Boolean>
    private val repository: SettingsRepository
    fun setActive(isChecked: Boolean) {
        repository.setActive(isChecked)
    }

    fun isActive(): LiveData<Boolean> {
        return repository.isActive
    }

    init {
        repository = SettingsRepository(application)
        active = repository.isActive
    }
}
