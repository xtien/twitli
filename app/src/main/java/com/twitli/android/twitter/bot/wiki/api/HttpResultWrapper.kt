package com.twitli.android.twitter.bot.wiki.api

class HttpResultWrapper {

    private var statusCode: Int = 0
    private var errorMessage: String? = null
    private var string: String? = null

    constructor(status: Int, msg: String?, string: String?) {
        statusCode = status
        this.string = string
        errorMessage = msg
    }

    fun getStatusCode(): Int {
        return statusCode
    }

    fun getString(): String? {
        return this.string
    }

}
