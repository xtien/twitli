package com.twitli.android.twitter.bot.wiki.type


abstract class Word {

    private var wordString: String? = null
     private lateinit var type: String

    fun setWordString(string: String) {
        this.wordString = string
    }

    fun getWordString(): String? {
        return wordString
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getType(): String {
        return type
    }
}
