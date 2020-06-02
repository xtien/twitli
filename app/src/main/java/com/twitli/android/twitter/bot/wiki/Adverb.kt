package com.twitli.android.twitter.bot.wiki

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.twitli.android.twitter.bot.wiki.type.Word

@Entity(tableName = "adverb", indices = [Index(value = ["positive"], unique = true)])
class Adverb : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "comparative")
    private var comparative: String? = null

    @ColumnInfo(name = "superlative")
    private var superlative: String? = null

    @ColumnInfo(name = "positive")
    private var positive: String? = null

    fun setPositive(string: String) {
        TODO("Not yet implemented")
    }

}
