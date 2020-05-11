/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.wiki;

import dagger.Component;

import java.io.IOException;

@Component
public interface WikiPageManager {

    void analyzePage(String string);

    void getPage(String year) throws IOException;
}
