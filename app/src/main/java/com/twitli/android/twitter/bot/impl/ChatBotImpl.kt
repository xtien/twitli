package com.twitli.android.twitter.bot.impl

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.bot.ChatBot
import twitter4j.Status
import java.util.concurrent.*

class ChatBotImpl constructor(application: Application): ChatBot {

    private var myName: String
    private val queue: BlockingQueue<Status> = LinkedBlockingQueue()
    private val es: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    var context: Context = application

    init {

        var prefs = context.getSharedPreferences("prefs", MODE_PRIVATE)
        myName = prefs.getString("my_user_name", "")!!;

        es.scheduleAtFixedRate({
            var status = queue.take()
            processStatus(status)
        }, 2, 2, TimeUnit.SECONDS)
    }

    private fun processStatus(status: Status) {
        TODO("Not yet implemented")
    }

    override fun read(tweets: List<Status?>?) {
        es.submit {
            tweets?.forEach {
                if (it != null && it.text.contains(myName)) {
                    queue.offer(it)
                }
            }
        }
    }
}
