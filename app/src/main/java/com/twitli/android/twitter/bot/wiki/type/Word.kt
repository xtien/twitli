package com.twitli.android.twitter.bot.wiki.type

import androidx.room.ColumnInfo


abstract class Word {

    @ColumnInfo(name = "wordString")
    var wordString: String = ""

    @ColumnInfo(name = "timeStamp")
    var timeStamp: Long = 0L

    @ColumnInfo(name = "timesUsed")
    var timesUsed: Long = 0L

    private var type: String = "string"

    fun setType(type: String) {
        this.type = type
    }

    fun getType(): String {
        return type
    }
}
