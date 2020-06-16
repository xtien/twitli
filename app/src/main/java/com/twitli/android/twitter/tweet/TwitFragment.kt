package com.twitli.android.twitter.tweet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.bot.ChatBot
import com.twitli.android.twitter.tweet.listener.OnInspectClickListener
import com.twitli.android.twitter.tweet.listener.OnLikeClickListener
import com.twitli.android.twitter.tweet.listener.OnReplyClickListener
import org.apache.commons.lang3.math.NumberUtils
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

class TwitFragment : Fragment(), OnLikeClickListener, OnReplyClickListener, OnInspectClickListener {

    private lateinit var listView: RecyclerView
    private lateinit var adapter: TwitAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var button: FloatingActionButton
    private lateinit var tweetView: LinearLayout
    private lateinit var submitButton: Button
    private lateinit var replyView: LinearLayout
    private lateinit var replyButton: Button
    private lateinit var tweetText: EditText
    private var isTweeting = false
    private lateinit var textLengthView: TextView
    private val tweets: MutableMap<Long, Tweet> = HashMap()
    private lateinit var replyText: EditText
    private lateinit var replyLengthView: TextView
    private lateinit var swipeContainer: SwipeRefreshLayout

    private var es = Executors.newScheduledThreadPool(2)

    @Inject
    lateinit var chatBot: ChatBot

    @Inject
    lateinit var twitViewModel: TwitViewModel

    @Inject
    lateinit var twitManager: TwitManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.twit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView = view.findViewById(R.id.listview)
        button = view.findViewById(R.id.tweet_button)
        submitButton = view.findViewById(R.id.submit_tweet)
        replyButton = view.findViewById(R.id.submit_reply)
        tweetView = view.findViewById(R.id.tweet)
        replyView = view.findViewById(R.id.reply)
        tweetText = view.findViewById(R.id.status_tweet_text)
        replyText = view.findViewById(R.id.reply_text)
        textLengthView = view.findViewById(R.id.text_length)
        replyLengthView = view.findViewById(R.id.reply_text_length)
        swipeContainer = view.findViewById(R.id.swiperefresh)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity!!.applicationContext as MyApplication).appComponent.inject(this)

        isTweeting = false

        twitViewModel.tweets?.observe(activity!!, Observer { tweets: List<Tweet?>? ->
            if (tweets != null) {
                for (tweet in tweets) {
                    if (tweet?.tweetId != null) {
                        this.tweets[tweet.tweetId] = tweet
                    }
                }
            }

            val list = ArrayList<Tweet>()
            list.addAll(this.tweets.values)
            adapter.setTweets(list)
            adapter.notifyDataSetChanged()
        })

        adapter = TwitAdapter()
        adapter.setOnLikeClickListener(this)
        adapter.setOnReplyClickListener(this)
        adapter.setOnInspectClickListener(this)
        layoutManager = LinearLayoutManager(activity)
        listView.layoutManager = layoutManager
        listView.adapter = adapter
        val tw: TextWatcher = MyTextWatcher(context!!, tweetText, textLengthView, submitButton)
        val tw2: TextWatcher = MyTextWatcher(context!!, replyText, replyLengthView, replyButton)
        tweetText.addTextChangedListener(tw)
        replyText.addTextChangedListener(tw2)
        button.setOnClickListener {
            if (!isTweeting) {
                listView.alpha = 0.5f
                tweetView.visibility = View.VISIBLE
                isTweeting = true
            } else {
                listView.alpha = 1.0f
                tweetView.visibility = View.INVISIBLE
                replyView.visibility = View.INVISIBLE
                isTweeting = false
            }
        }
        swipeContainer.setOnRefreshListener {
            es.execute {
                twitViewModel.loadTweets()
                swipeContainer.isRefreshing = false
            }
        }
        submitButton.setOnClickListener { doEdit(tweetView, tweetText) }
    }

    private fun doEdit(view: LinearLayout?, textview: TextView?) {
        doEdit(view, textview, null)
    }

    private fun doEdit(replyView: LinearLayout?, replyText: TextView?, tweetId: Long?) {
        listView!!.alpha = 1.0f
        replyView!!.visibility = View.INVISIBLE
        hideKeyboard(activity)
        if (replyText!!.text != null && replyText.text.toString() != null) {
            val message = replyText.text.toString()
            val words = message.split(" ".toRegex()).toTypedArray()
            for (w in words) {
                var word = w.trim { it <= ' ' }
                if (word.length > 2 && NumberUtils.isCreatable(word)) {
                    val icycle = Intent()
                    icycle.action = "nl.christine.app.message"
                    icycle.putExtra("message", word)
                    activity!!.sendBroadcast(icycle)
                }
            }
            if (tweetId == null) {
                twitManager.tweet(replyText.text.toString())
            } else {
                twitManager.reply(replyText.text.toString(), tweetId)
            }
            replyText.text = ""
        }
    }

    override fun onLikeClicked(tweetId: Long, liked: Boolean) {
        twitViewModel.onLikeClicked(tweetId, liked)
        es.execute { if (liked) twitManager.unlike(tweetId) else twitManager.like(tweetId) }
        es.schedule({
            activity!!.runOnUiThread { adapter.notifyDataSetChanged() }
        }, 200L, TimeUnit.MILLISECONDS)
    }

    override fun onReplyClicked(tweetId: Long?) {
        listView!!.alpha = 0.5f
        replyView!!.visibility = View.VISIBLE
        replyButton!!.setOnClickListener { v: View? -> doEdit(replyView, replyText, tweetId) }
    }

    companion object {
        fun newInstance(): Fragment {
            return TwitFragment()
        }

        fun hideKeyboard(activity: Activity?) {
            val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onInspectClick(tweetId: Long) {

        var tweet: Tweet? = tweets.get(tweetId)
        if (tweet != null) {
            chatBot.processTweet(tweet)
        }
    }
}
