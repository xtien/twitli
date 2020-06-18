/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

import com.twitli.android.twitter.bot.dict.type.Word

class Pattern(question: String, vararg words: String) {

    val wordTypes: Array<out String> = words
    private val question = question

    fun matches(list: List<List<Word>>): Boolean {
        var iterator = list.iterator()
        var patternIterator = wordTypes.iterator()
        var count = 0
        var i = iterator.next()
        var p = patternIterator.next()
        while (count < wordTypes.size) {
            if (isOfType(i, p)) {
                count++
                if (count >= wordTypes.size) {
                    return true
                }
                if (iterator.hasNext()) {
                    i = iterator.next()
                } else {
                    return false
                }
                if (patternIterator.hasNext()) {
                    p = patternIterator.next()
                } else {
                    return false
                }
            } else {
                if (!iterator.hasNext()) {
                    return false
                }
                i = iterator.next()
                while (patternIterator.hasNext() && p == "any") {
                    p = patternIterator.next()
                }
                while (iterator.hasNext() && isOfType(i, "string")) {
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

    fun hasQuestion(): Boolean {
        return question != null
    }

    fun toListString(lists: List<List<Word>>): String {
        val words = mutableListOf<Word>()
        for (list in lists) {
            words.add(list[0])
        }
        return toString(words)
    }

    fun toString(words: List<Word>): String {

        var args: ArrayList<String> = arrayListOf<String>()

        var wordIterator = words.iterator()
        var patternIterator = wordTypes.iterator()

        loop@ while (patternIterator.hasNext()) {
            val wordType = patternIterator.next()
            while(wordIterator.hasNext()){
                var word = wordIterator.next()
                if(word.type == wordType){
                    args.add(word.wordString)
                    break
                }
            }
        }

        return String.format(question, *args.toArray())
    }
}
