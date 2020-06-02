package com.twitli.android.twitter.bot.wiki

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.twitli.android.twitter.bot.wiki.type.Word
import org.apache.commons.lang3.builder.HashCodeBuilder

@Entity(tableName = "verb", indices = [Index(value = ["presentTense", "infinitive"], unique = true)])
class Verb : Word() {

    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Long? = null

    @ColumnInfo(name = "presentTense")
    private var presentTense: String? = null

    @ColumnInfo(name = "presentTenseFirstPersonSingular")
    val presentTenseFirstPersonSingular: String? = null

    @ColumnInfo(name = "presentTenseThirdPersonSingular")
    private var presentTenseThirdPersonSingular: String? = null

    @ColumnInfo(name = "presentTensePlural")
    var presentTensePlural: String? = null

    @ColumnInfo(name = "presentParticiple")
    var presentParticiple: String? = null

    @ColumnInfo(name = "gerund")
    var gerund: String? = null

    @ColumnInfo(name = "infinitive")
    var infinitive   : String? = null

    @ColumnInfo(name = "pastParticiple")
    var pastParticiple: String? = null

    @ColumnInfo(name = "pastTense")
    var pastTense: String? = null

    @ColumnInfo(name = "pastTensePlural")
    var pastTensePlural: String? = null

    fun setPresentTense(string: String?) {
        presentTense = string
    }

    override fun toString(): String {
        return presentTenseThirdPersonSingular!!
    }

    override fun hashCode(): Int {
        return HashCodeBuilder().append(presentTense).append(37).toHashCode()
    }

    fun setPresentTenseThirdPersonSingular(string: String) {
        this.presentTenseThirdPersonSingular = string
    }

}
