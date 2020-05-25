package com.twitli.android.twitter.tweet.listener

interface OnLikeClickListener {
    fun onLikeClicked(tweetId: Long?, liked: Boolean)
}
