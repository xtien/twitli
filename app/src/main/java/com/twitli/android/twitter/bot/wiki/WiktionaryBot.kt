package com.twitli.android.twitter.bot.wiki

import com.twitli.android.twitter.bot.dict.type.Word
import com.twitli.android.twitter.tweet.Tweet
import java.io.IOException
import java.sql.SQLException


/**
 * looks up wiktionary for grammatical meaning of a word
 *
 * @author christine
 */
interface WiktionaryBot {
    /**
     * find type of word for wordString. It finds all potential types.
     *
     * @param string
     * @return
     * @throws IOException
     */
    @Throws(IOException::class, SQLException::class)
    fun classify(string: String): List<Word>

    fun getType(string: String): List<Word>

    fun getWords(tweet: Tweet): List<List<Word>>
}
