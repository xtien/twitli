package com.twitli.android.twitter.bot.wiki.type


open class Word  {

    private var wordString: String? = null

    fun setWordString(string: String) {
        this.wordString = string
    }

    fun getWordString(): String? {
        return wordString
    }
}
