/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitli.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings_table")
public class MySettings {

    @PrimaryKey
    private int id;

    @ColumnInfo(name="settingsid")
    private int settingsId;

    @ColumnInfo(name="active")
    private Boolean active;

    @ColumnInfo(name="followers")
    private int followers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(int settingsId) {
        this.settingsId = settingsId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
