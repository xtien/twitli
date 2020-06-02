package com.twitli.android.twitter.bot.wiki

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.twitli.android.twitter.bot.wiki.type.Word

@Entity(tableName = "adjective", indices = [Index(value = ["positive"], unique = true)])
class Adjective : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "superlative")
    private val superlative: String? = null

    @ColumnInfo(name = "comparative")
    private val comparative: String? = null

    @ColumnInfo(name = "positive")
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
