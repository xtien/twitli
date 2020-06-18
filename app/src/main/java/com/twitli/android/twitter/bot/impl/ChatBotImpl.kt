package com.twitli.android.twitter.bot.impl

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.twitli.android.twitter.R
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.PatternResult
import com.twitli.android.twitter.bot.dict.Pattern
import com.twitli.android.twitter.bot.dict.Patterns
import com.twitli.android.twitter.bot.dict.type.Word
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.tweet.Tweet
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.tweet.TwitRepository
import java.lang.Long.max
import java.lang.Long.min
import java.util.concurrent.*

class ChatBotImpl constructor(application: Application, wikBot: WiktionaryBot, twit: TwitManager, twitRepository: TwitRepository) : ChatBot {

    private var chatWindow: Long
    private var prefs: SharedPreferences
    private val LOGTAG: String = javaClass.name

    private val typeString: String = "%s: %s."
    private var wikBot: WiktionaryBot = wikBot
    private var twitRepository = twitRepository
    private var context: Context = application
    private val twit = twit

    private var myUserId: Long
    private var myName: String
    private val es: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    init {

        chatWindow = context.resources.getInteger(R.integer.chat_window).toLong() * 1000L
        prefs = context.getSharedPreferences("prefs", MODE_PRIVATE)
        myUserId = prefs.getLong("user_id", 0L)
        myName = prefs.getString("my_name", "").toString()

        es.scheduleAtFixedRate({
            var status = takeStatus()
            if (status != null) {
                processTweet(status)
            }
        }, 6, 10, TimeUnit.SECONDS)
    }

    private fun takeStatus(): Tweet {
        return twitRepository.getNewTweet()
    }

    override fun processTweet(tweet: Tweet) {

        twitRepository.setTweetDone(tweet.tweetId)
        if (System.currentTimeMillis() < (min(tweet.time, prefs.getLong("install_time", 0L)) + chatWindow)) {
            es.submit {
                Log.d(LOGTAG, "processing: " + tweet.text)
                var tweetWords = learnWords(tweet)
                var patternResult = learnSentence(tweetWords)

                if (tweet.screenName != myName)
                    if (patternResult.pattern != null) {
                        val pattern = patternResult.pattern
                        if (pattern != null && pattern.hasQuestion()) {
                            twit.tweet(pattern.toListString(tweetWords))
                        } else {
                            var stringList = sentenceWords(patternResult.words)
                            twit.tweet(makeString(stringList))
                        }
                    }
            }
        }
    }

    private fun learnWords(tweet: Tweet): List<List<Word>> {
        return wikBot.getWords(tweet)
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

    private fun learnSentence(words: List<List<Word>>): PatternResult {
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
