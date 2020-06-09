/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

import com.twitli.android.twitter.bot.dict.type.Word

class Pattern(vararg words: String) {

    fun matches(list: List<List<Word>>): Boolean {
        var iterator = list.iterator()
        var patternIterator = wordTypes.iterator()
        var count = 0
        while (iterator.hasNext() && patternIterator.hasNext()) {
           var i = iterator.next()
           var p = patternIterator.next()
            count++
            if (isOfType(i, p)) {
            } else if (p == "any") {
                p = patternIterator.next()
                count++
                if (!isOfType(i, p)) {
                    i = iterator.next()
                }
            }
        }
        return count == wordTypes.size
    }

    private fun isOfType(words: List<Word>, type: String): Boolean {
        for (word in words) {
            if (word.type == type) {
                return true
            }
            if (word.type == "string" && type == "any") {
                return true
            }
        }
        return false
    }

    private val wordTypes: Array<out String> = words
}
