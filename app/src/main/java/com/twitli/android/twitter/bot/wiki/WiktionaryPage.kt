package com.twitli.android.twitter.bot.wiki

import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Class describes a wiktionary page and has methods for retrieving info from
 * that page.
 *
 * @author christine
 */
class WiktionaryPage(private val string: String) {
    private val sectionString = "<span class=\"mw-headline\""

    /**
     * parse a wiktionary page for a particular word and it's gramatical types.
     *
     * @return
     */
    fun parseForWord(): List<String> {
        val matchingString = sectionString
        val result: MutableList<String> = ArrayList()
        var index = 0
        var subString = string
        index = StringUtils.indexOf(subString, matchingString)
        while (index != -1) {
            if (index != -1) {
                subString = subString.substring(index + matchingString.length)
                var typeString = subString.substring(subString.indexOf(">") + 1, subString.length)
                typeString = typeString.substring(0, typeString.indexOf("<"))
                if (typeString != null) {
                    if (typeString.equals("Verb", ignoreCase = true)
                            || typeString.equals("Noun", ignoreCase = true)
                            || typeString.equals("Article", ignoreCase = true)
                            || typeString.equals("Number", ignoreCase = true)
                            || typeString.equals("Adjective", ignoreCase = true)
                            || typeString.equals("Preposition", ignoreCase = true)
                            || typeString.equals("Conjuntive", ignoreCase = true)
                            || typeString.equals("Interjection", ignoreCase = true)
                            || typeString.equals("Personal Pronoun", ignoreCase = true)
                            || typeString.equals("Proper Noun", ignoreCase = true)
                            || typeString.equals("Adverb", ignoreCase = true)) {
                        if (!result.contains(typeString)) {
                            result.add(typeString)
                        }
                    } else if (typeString.equals("References", ignoreCase = true)) {
                        return result
                    }
                }
            }
            index = StringUtils.indexOf(subString, matchingString)
        }
        return result
    }

}
