/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.wiki;

import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.data.ContentRepository;
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
    public String getPage(String year) throws IOException {

        String result = null;

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
                    text = fromHtml(text.length() < 271 ? text : text.substring(0, 270));
                    repository.addContent(year, datum, text);
                    if (result == null) {
                        result = year + ", " + datum + ": "+ text;
                    }
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
                    text = fromHtml(text.length() < 270 ? text : text.substring(0, 270));
                    repository.addContent(year, text);
                    if (result == null) {
                        result = year + ": "+ text;
                    }
                }
            }
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public static String fromHtml(String html) {
        if (html == null) {
            return "";
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }
}
