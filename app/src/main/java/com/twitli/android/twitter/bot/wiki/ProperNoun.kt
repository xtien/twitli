package com.twitli.android.twitter.bot.wiki

import com.twitli.android.twitter.bot.wiki.type.Word


class ProperNoun : Word {

    private var name: String? = null

    constructor() {}

    constructor(name: String) {
        this.name = name
        wordString = name
    }

    fun setName(string: String) {
        this.name = string
    }

}
