/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.tweet

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.twitli.android.twitter.R
import com.twitli.android.twitter.data.BaseViewHolder
import com.twitli.android.twitter.tweet.listener.OnInspectClickListener
import com.twitli.android.twitter.tweet.listener.OnLikeClickListener
import com.twitli.android.twitter.tweet.listener.OnReplyClickListener
import org.apache.commons.lang3.math.NumberUtils
import org.apache.commons.lang3.time.FastDateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TwitAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private lateinit var sFormat: SimpleDateFormat
    var list: List<Tweet> = ArrayList()
    lateinit var format: FastDateFormat
    private lateinit var context: Context
    private lateinit var onLikeClickListener: OnLikeClickListener
    private lateinit var onReplyClickListener: OnReplyClickListener
    private lateinit var onInspectClickListener: OnInspectClickListener

    fun setOnLikeClickListener(listener: OnLikeClickListener) {
        onLikeClickListener = listener
    }

    fun setOnReplyClickListener(listener: OnReplyClickListener) {
        onReplyClickListener = listener
    }

    fun setOnInspectClickListener(listener: OnInspectClickListener) {
        onInspectClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (!this::context.isInitialized) {
            context = parent.context
        }
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.tweet_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setTweets(tweets: List<Tweet>) {
        this.list = tweets

        Collections.sort(tweets, object : Comparator<Tweet?> {
            override fun compare(o1: Tweet?, o2: Tweet?): Int {
                if (o2 == null) {
                    return 1;
                } else if (o1 == null) {
                    return 1;
                }
                return (o2.time!!.toLong() - o1.time!!.toLong()).toInt()
            }
        })
    }

    private inner class ViewHolder(view: View) : BaseViewHolder(view) {
        var numberOfLikes: TextView
        var twitterIdView: TextView
        var timeView: TextView
        var nameView: TextView
        var tweetTextView: TextView
        var like: ImageView
        var reply: ImageView
        var inspect: ImageView

        override fun onBind(position: Int) {
            super.onBind(position)

            val tweet = list[position]
            if (tweet.isLiked) {
                like.setImageResource(R.drawable.ic_favorite_black_12dp)
            } else {
                like.setImageResource(R.drawable.ic_favorite_border_black_12dp)
            }
            numberOfLikes.text = if (tweet.likes > 0) Integer.toString(tweet.likes) else ""
            twitterIdView.text = tweet.screenName
            if (tweet.time != 0L) {
                val date = Date(tweet.time)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    timeView.text = format!!.format(date)
                } else {
                    timeView.text = sFormat!!.format(date)
                }
            }
            nameView.text = tweet.name.toString()
            tweetTextView.text = tweet.text.toString()

            if (tweet.text!!.length == 280) {
                tweetTextView.setTextColor(ContextCompat.getColor(context!!, R.color.tweet_highlight))
            } else {
                tweetTextView.setTextColor(ContextCompat.getColor(context!!, R.color.tweet_text))
            }
            like.setOnClickListener {
                onLikeClickListener.onLikeClicked(tweet.tweetId, tweet.isLiked)
             }
            reply.setOnClickListener { onReplyClickListener.onReplyClicked(tweet.tweetId) }
            inspect.setOnClickListener {onInspectClickListener.onInspectClick(tweet.tweetId)}
        }

        override fun clear() {
            twitterIdView.text = ""
            timeView.text = ""
            nameView.text = ""
            tweetTextView.text = ""
        }

        init {
            twitterIdView = view.findViewById(R.id.twitter_id)
            timeView = view.findViewById(R.id.time)
            nameView = view.findViewById(R.id.twitter_name)
            tweetTextView = view.findViewById(R.id.status_text)
            like = view.findViewById(R.id.icon_like)
            reply = view.findViewById(R.id.icon_reply)
            numberOfLikes = view.findViewById(R.id.number_of_likes)
            inspect = view.findViewById(R.id.icon_inspect)
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            format = FastDateFormat.getInstance("MM-dd kk:mm", TimeZone.getDefault(), Locale.getDefault())
        } else {
            sFormat = SimpleDateFormat("MM-dd kk:mm", Locale.getDefault())
        }
    }
}
