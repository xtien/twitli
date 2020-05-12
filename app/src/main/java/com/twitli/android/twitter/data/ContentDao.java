/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContentDao {

    @Insert
    public void insert(Content content);

    @Query("insert into content_table (year, date, text, used) values (:year, :datum, :text, 0)")
    void addContent(String year, String datum, String text);

    @Query("insert into content_table (year, text, used) values (:year,  :text, 0)")
    void addContent(String year, String text);

    @Query("select * from content_table where NOT used and year = :year")
    List<Content> getFirst(String year);

    @Query("select * from content_table where NOT used and year = :year")
    List<Content> getAvailable(int year);

    @Query("select * from content_table where used AND year = :year")
    List<Content> getDone(int year);

    @Query("update content_table set used = 1 where id = :id")
    void setDone(int id);
}
