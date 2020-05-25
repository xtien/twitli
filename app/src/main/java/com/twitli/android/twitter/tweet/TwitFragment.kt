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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.dagger.TwitViewModelFactory
import com.twitli.android.twitter.tweet.listener.OnLikeClickListener
import com.twitli.android.twitter.tweet.listener.OnReplyClickListener
import org.apache.commons.lang3.math.NumberUtils
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.ArrayList

class TwitFragment : Fragment(), OnLikeClickListener, OnReplyClickListener {

    private var twitViewModel: TwitViewModel? = null
    private var listView: RecyclerView? = null
    private var adapter: TwitAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var button: FloatingActionButton? = null
    private var tweetView: LinearLayout? = null
    private var submitButton: Button? = null
    private var replyView: LinearLayout? = null
    private var replyButton: Button? = null
    private var logoutButton: Button? = null
    private var tweetText: EditText? = null
    private var isTweeting = false
    private var textLengthView: TextView? = null
    private val tweets: MutableMap<Long, Tweet> = HashMap()
    private var replyText: EditText? = null
    private var replyLengthView: TextView? = null
    private var swipeContainer: SwipeRefreshLayout? = null

    var es = Executors.newCachedThreadPool()

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
        tweetText = view.findViewById(R.id.status_text)
        replyText = view.findViewById(R.id.reply_text)
        textLengthView = view.findViewById(R.id.text_length)
        replyLengthView = view.findViewById(R.id.reply_text_length)
        swipeContainer = view.findViewById(R.id.swiperefresh)
        logoutButton = view.findViewById(R.id.logout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity!!.applicationContext as MyApplication).appComponent!!.inject(this)

        isTweeting = false

        val factory = TwitViewModelFactory(activity!!.application, twitManager)
        twitViewModel = ViewModelProvider(this, factory).get(TwitViewModel::class.java)
        twitViewModel!!.tweets?.observe(activity!!, Observer { tweets: List<Tweet?>? ->
            if (tweets != null) {
                for (tweet in tweets) {
                    if (tweet != null && tweet.tweetId != null) {
                        this.tweets.put(tweet.tweetId!!, tweet)
                    }
                }
            }

            val list = ArrayList<Tweet>()
            list.addAll(this.tweets.values)
            adapter!!.setTweets(list)
            adapter!!.notifyDataSetChanged()
        })

        adapter = TwitAdapter()
        adapter!!.setOnLikeClickListener(this)
        adapter!!.setOnReplyClickListener(this)
        layoutManager = LinearLayoutManager(activity)
        listView!!.layoutManager = layoutManager
        listView!!.adapter = adapter
        val tw: TextWatcher = MyTextWatcher(context!!, tweetText!!, textLengthView!!, submitButton!!)
        val tw2: TextWatcher = MyTextWatcher(context!!, replyText!!, replyLengthView!!, replyButton!!)
        tweetText!!.addTextChangedListener(tw)
        replyText!!.addTextChangedListener(tw2)
        button!!.setOnClickListener { v: View? ->
            if (!isTweeting) {
                listView!!.alpha = 0.5f
                tweetView!!.visibility = View.VISIBLE
                isTweeting = true
            } else {
                listView!!.alpha = 1.0f
                tweetView!!.visibility = View.INVISIBLE
                replyView!!.visibility = View.INVISIBLE
                isTweeting = false
            }
        }
        swipeContainer!!.setOnRefreshListener {
            es.execute {
                twitViewModel!!.loadTweets()
                swipeContainer!!.isRefreshing = false
            }
        }
        submitButton!!.setOnClickListener { v: View? -> doEdit(tweetView, tweetText) }
        logoutButton!!.setOnClickListener { v: View? -> logout() }
    }

    private fun logout() {
        twitManager.logout()
        activity?.finish()
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
                if (word != null && word.length > 2 && NumberUtils.isCreatable(word)) {
                    val icycle = Intent()
                    icycle.action = "nl.christine.app.message"
                    icycle.putExtra("message", word)
                    activity!!.sendBroadcast(icycle)
                }
            }
            if (tweetId == null) {
                twitManager!!.tweet(replyText.text.toString())
            } else {
                twitManager!!.reply(replyText.text.toString(), tweetId)
            }
            replyText.text = ""
        }
    }

    override fun onLikeClicked(tweetId: Long?, liked: Boolean) {
        es.execute { if(liked)  twitManager!!.like(tweetId) else twitManager.unlike(tweetId) }
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
}
