package com.twitli.android.twitter.bot.dict.type

import androidx.room.ColumnInfo


abstract class Word {

    @ColumnInfo(name = "wordString")
    var wordString: String = ""

    @ColumnInfo(name = "timeStamp")
    var timeStamp: Long = 0L

    @ColumnInfo(name = "timesUsed")
    var timesUsed: Long = 0L

    @ColumnInfo(name = "type")
    var type: String = "string"
}
