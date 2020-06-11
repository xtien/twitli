package com.twitli.android.twitter.bot.dict.type


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
