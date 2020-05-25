package com.twitli.android.twitter.bot.wiki.type


import org.apache.commons.lang3.builder.HashCodeBuilder

class Noun : Word() {
    private var singular: String? = null

    private var plural: String? = null

    override fun toString(): String {
        return singular!!
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37).append(singular).append(plural).toHashCode()
    }

    fun setSingular(string: String) {
        this.singular = string
    }
}
