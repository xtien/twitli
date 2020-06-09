package com.twitli.android.twitter.bot.impl

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.bot.wiki.WiktionaryBot
import com.twitli.android.twitter.bot.dict.type.Word
import twitter4j.Status
import java.util.concurrent.*

class ChatBotImpl constructor(application: Application, wikBot: WiktionaryBot) : ChatBot {

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

    private fun processStatus(status: Status) {
        var words = wikBot.getWords(status)
        var pattern = analyzeSentence(words)
    }

    private fun analyzeSentence(words: List<List<Word>>): Any {
        TODO("Not yet implemented")
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
