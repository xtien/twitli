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

@Entity(tableName = "content_table")
public class Content {

    @PrimaryKey
    private int id;

    @ColumnInfo(name="year")
    private String year;

    @ColumnInfo(name="date")
    private String date;

    @ColumnInfo(name="text")
    private String text;

    @ColumnInfo(name="used")
    private boolean used;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
