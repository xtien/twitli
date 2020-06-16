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
                Pattern("What about the %2\$s %3\$s?", "verb", "any", "adjective", "noun"),
                Pattern("Tell me about the %2\$s %1\$s?", "adjective", "noun"),
                Pattern("What about the %3\$s %1\$s?", "noun", "verb", "adjective"),
                Pattern("", "noun", "any", "noun"),
                Pattern("", "noun", "any", "verb", "any", "noun"),
                Pattern("%s flowers...", "number")
        )
    }
}
