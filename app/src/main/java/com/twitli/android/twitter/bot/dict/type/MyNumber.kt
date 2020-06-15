package com.twitli.android.twitter.bot.dict.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "number", indices = [Index(value = ["wordString"], unique = true)])
class MyNumber : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null
}
