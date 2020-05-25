package com.twitli.android.twitter.bot.wiki

import com.twitli.android.twitter.bot.wiki.type.Word
import org.apache.commons.lang3.builder.HashCodeBuilder

class Verb : Word() {

     private var presentTense: String? = null

     val presentTenseFirstPersonSingular: String? = null

   private var presentTenseThirdPersonSingular: String? = null

    val presentTensePlural: String? = null

    val presentParticiple: String? = null

    val gerund: String? = null

    val infinitive // without "to"
            : String? = null
        get() = field ?: presentTense

    val pastParticiple: String? = null

    var pastTense: String? = null

    val pastTensePlural: String? = null

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
