package com.twitli.android.twitter.bot.impl

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.dict.Pattern
import com.twitli.android.twitter.bot.dict.Patterns
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.dict.type.Word
import twitter4j.Status
import java.util.concurrent.*

class ChatBotImpl constructor(application: Application, wikBot: WiktionaryBot) : ChatBot {

    private val typeString: String = "%s: %s."
    private var wikBot: WiktionaryBot = wikBot
    private var context: Context = application

    private var myUserId: Long
    private val queue: BlockingQueue<Status> = LinkedBlockingQueue()
    private val es: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    init {

        var prefs = context.getSharedPreferences("prefs", MODE_PRIVATE)
        myUserId = prefs.getLong("my_user_id", 0L)!!;

        es.scheduleAtFixedRate({
            var status = queue.take()
            processStatus(status)
        }, 2, 2, TimeUnit.SECONDS)
    }

    override fun processStatus(status: Status): List<Word> {
        var words = wikBot.getWords(status)
        var analysis = analyzeSentence(words)
        var string = sentenceWords(analysis)
        return analysis
    }

    override fun sentenceWords(words: List<Word>): List<String> {

        var resultList = mutableListOf<String>()
        for (word in words) {
            resultList.add(String.format(typeString, word.type, word.wordString))
        }
        return resultList.toList()
    }

    private fun analyzeSentence(words: List<List<Word>>): List<Word> {
        var result = mutableListOf<Word>()
        for (p in Patterns.patterns) {
            if (p.matches(words)) {
                return processPattern(words, p);
            }
        }
        return result
    }

    private fun processPattern(words: List<List<Word>>, p: Pattern): List<Word> {

        var wordIterator = words.iterator()
        var patternIterator = p.wordTypes.iterator()

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

        return resultList
    }

    override fun read(tweets: List<Status?>?) {
        es.submit {
            tweets?.forEach {
                if (it != null && it.inReplyToStatusId == myUserId) {
                    queue.offer(it)
                }
            }
        }
    }
}
