/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers;

import androidx.test.core.app.ApplicationProvider;
import nl.christine.twitfollowers.data.Content;
import nl.christine.twitfollowers.data.ContentRepository;
import nl.christine.twitfollowers.wiki.WikiPageManager;
import nl.christine.twitfollowers.wiki.WikiPageManagerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestGetPage {

    ContentRepository contentRepository;

    @Before
    public void createDb() {
        contentRepository = new ContentRepository(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void getPage() throws IOException {

        WikiPageManager wikiPageManager = new WikiPageManagerImpl();
        wikiPageManager.getPage("1");

        Content content = contentRepository.getFirst(1);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.getText().contains("Aarde"));
        contentRepository.setDone(content.getId());

        content = contentRepository.getFirst(1);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.getText().contains("China"));
    }
}
