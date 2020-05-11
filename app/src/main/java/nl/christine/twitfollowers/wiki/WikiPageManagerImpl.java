/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.wiki;

import nl.christine.twitfollowers.MyApplication;
import nl.christine.twitfollowers.data.ContentRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WikiPageManagerImpl implements WikiPageManager {

    private static final String LOGTAG = WikiPageManagerImpl.class.getSimpleName();
    private String baseUrl = "https://nl.wikipedia.org/wiki/";
    private ContentRepository repository;

    public WikiPageManagerImpl() {
        repository = new ContentRepository(MyApplication.getApplication());
    }

    @Override
    public void analyzePage(String string) {

    }

    @Override
    public void getPage(String year) throws IOException {

        int numberOfItems = 0;
        Document doc = Jsoup.connect(baseUrl + year).get();
        Elements elements = doc.select("h2, li, ul");
        boolean scan = false;
        for (Element e : elements) {
            String string = e.toString();

            if (scan && string.contains("<h2>")) {
                break;
            }
            if (string.contains("<h2>") && string.contains("Gebeurtenissen")) {
                scan = true;
            }

            if (scan && string.startsWith("<li><a href=") && string.contains("title=")) {
                String text = e.text();
                String datum = string.substring(string.indexOf("title=") + 7, string.indexOf("\">"));
                if (datum.matches("[0-9]{1,2} [a-zA-Z]{3,12}")) {
                    if (text.contains(" - ")) {
                        text = text.substring(text.indexOf(" - ") + 3);
                    }
                    repository.addContent(year, datum, text);
                    numberOfItems++;
                }
            }
        }

        if (numberOfItems == 0) {
            scan = false;
            for (Element e : elements) {
                String string = (e.toString());
                if (scan && string.contains("<h2>")) {
                    break;
                }
                if (string.contains("<h2>") && string.contains("Gebeurtenissen")) {
                    scan = true;
                }
                if (scan && string.startsWith("<li><a href=") && string.contains("title=")) {
                    String text = e.text();
                    repository.addContent(year, text.length() < 281 ? string : text.substring(0, 280));
                }
            }
        }
    }
}
