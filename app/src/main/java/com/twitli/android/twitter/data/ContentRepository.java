/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.data;

import android.app.Application;

import java.util.List;

public class ContentRepository {

    private ContentDao contentDao;

    public ContentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        contentDao = db.contentDao();
    }

    public void addContent(String year, String datum, String text) {
        contentDao.addContent(year, datum, text);
    }

    public void addContent(String year, String text) {
        contentDao.addContent(year, text);
    }

    public Content getFirst(Integer year) {
        return contentDao.getFirst(year);
    }

    public void setDone(int id) {
        contentDao.setDone(id);
    }

    public ContentStatus getStatus(int year) {
        List<Content> available = contentDao.getAvailable(year);
        List<Content> done = contentDao.getDone(year);
        if (available.size() > 0) {
            return ContentStatus.AVAILABLE;
        } else {
            if (done.size() > 0) {
                return ContentStatus.DONE;
            } else {
                return ContentStatus.NONE;
            }
        }
    }

    public List<Content> getAvailable(int year) {
        return contentDao.getAvailable(year);
    }
}
