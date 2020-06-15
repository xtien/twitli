/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

class Patterns() {

    companion object {
        val patterns: List<Pattern> = mutableListOf(
                Pattern(null, "verb", "any", "adjective", "noun"),
                Pattern(null, "adjective", "noun"),
                Pattern(null, "noun", "verb", "adjective"),
                Pattern(null, "noun", "any", "noun"),
                Pattern(null, "noun", "any", "verb", "any", "noun"),
                Pattern(null, "number")
        )
    }
}
