package com.twitli.android.twitter.bot.impl

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.PatternResult
import com.twitli.android.twitter.bot.dict.Pattern
import com.twitli.android.twitter.bot.dict.Patterns
import com.twitli.android.twitter.bot.dict.type.Word
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.tweet.Tweet
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitRepository
import java.util.concurrent.*

class ChatBotImpl constructor(application: Application, wikBot: WiktionaryBot, twit: TwitManager, twitRepository: TwitRepository) : ChatBot {

    private val LOGTAG: String = javaClass.name

    private val typeString: String = "%s: %s."
    private var wikBot: WiktionaryBot = wikBot
    private var twitRepository = twitRepository
    private var context: Context = application
    private val twit = twit

    private var myUserId: Long
    private val es: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    init {

        var prefs = context.getSharedPreferences("prefs", MODE_PRIVATE)
        myUserId = prefs.getLong("my_user_id", 0L);

        es.scheduleAtFixedRate({
            var status = takeStatus()
            if (status != null) {
                processTweet(status)
            }
        }, 2, 2, TimeUnit.SECONDS)
    }

    private fun takeStatus(): Tweet {
        return twitRepository.getNewTweet()
    }

    override fun processTweet(tweet: Tweet) {
        Log.d(LOGTAG, "processing: " + tweet.text)
        var words = wikBot.getWords(tweet)
        var analysis = analyzeSentence(words)
        if (analysis.pattern != null) {
            if (analysis.pattern!!.hasQuestion()) {

            } else {
                var stringList = sentenceWords(analysis.words)
                twit.tweet(makeString(stringList))
            }
        }
        twitRepository.setTweetDone(tweet.tweetId)
    }

    private fun makeString(stringList: List<String>): String {
        var result = ""
        for (s in stringList) {
            result += s + "\n"
        }
        return result.substring(0, result.length - 1)
    }

    override fun sentenceWords(words: List<Word>): List<String> {

        var resultList = mutableListOf<String>()
        for (word in words) {
            resultList.add(String.format(typeString, word.type, word.wordString))
        }
        return resultList.toList()
    }

    private fun analyzeSentence(words: List<List<Word>>): PatternResult {
        var result = PatternResult(null, mutableListOf())
        for (p in Patterns.patterns) {
            if (p.matches(words)) {
                return processPattern(words, p);
            }
        }
        return result
    }

    private fun processPattern(words: List<List<Word>>, pat: Pattern): PatternResult {

        var wordIterator = words.iterator()
        var patternIterator = pat.wordTypes.iterator()

        var resultList = mutableListOf<Word>()
        while (patternIterator.hasNext()) {
            var p = patternIterator.next()
            loop@ while (wordIterator.hasNext()) {
                var wordList = wordIterator.next()
                for (word in wordList) {
                    if (p.equals(word.type)) {
                        resultList.add(word)
                        break@loop
                    }
                }
            }
        }

        return PatternResult(pat, resultList)
    }
}
