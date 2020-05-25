package com.twitli.android.twitter.bot.wiki

import com.twitli.android.twitter.bot.wiki.type.Word

class Adjective : Word() {

    private val superlative: String? = null
    private val comparative: String? = null
     private var positive: String? = null

    fun Adjective(string: String) {
        positive = string
        setWordString(string)
    }

    fun getPositive(): String? {
        return positive
    }

    fun getComparative(): String? {
        return comparative
    }

    fun getSuperlative(): String? {
        return superlative
    }

    fun setPositive(positive: String?) {
        this.positive = positive
    }

    fun getString(): String? {
        return positive
    }
}
