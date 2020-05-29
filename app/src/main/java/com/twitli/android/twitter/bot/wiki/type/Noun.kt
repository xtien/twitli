package com.twitli.android.twitter.bot.wiki.type


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.apache.commons.lang3.builder.HashCodeBuilder

@Entity(tableName = "noun", indices = [Index(value = ["singular", "plural"], unique = true)])
class Noun : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "singular")
    private var singular: String = ""

    @ColumnInfo(name = "plural")
    private var plural: String = ""

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37).append(singular).append(plural).toHashCode()
    }

    fun setSingular(string: String) {
        this.singular = string
    }

    fun getSingular(): String {
        return this.singular
    }

    fun getPlural(): String {
        return this.plural
    }

    fun setPlural(plural:String){
        this.plural = plural
    }
}
