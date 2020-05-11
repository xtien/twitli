/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContentRepository {

    private ContentDao contentDao;

    public ContentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        contentDao = db.contentDao();
    }

    public void addContent(String year, String datum, String text) {
        contentDao.addContent(year,datum,text);
    }

    public void addContent(String year,  String text) {
        contentDao.addContent(year,text);
    }

    public Content getFirst(Integer year) {
        return contentDao.getFirst(year);
    }

    public void setDone(int id) {
        contentDao.setDone(id);
    }
}
