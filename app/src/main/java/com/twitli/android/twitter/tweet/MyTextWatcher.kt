package com.twitli.android.twitter.tweet

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.twitli.android.twitter.R

class MyTextWatcher(private val context: Context, private val tweetText: EditText, private val textLengthView: TextView, private val submitButton: Button) : TextWatcher {
    private val maxTweetLength = 280
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        textLengthView.text = Integer.toString(s.length)
        if (s.length >= maxTweetLength) {
            textLengthView.setTextColor(ContextCompat.getColor(context, R.color.text_count_full))
            tweetText.setTextColor(ContextCompat.getColor(context, R.color.tweet_highlight))
        } else {
            textLengthView.setTextColor(ContextCompat.getColor(context, R.color.text_count))
            tweetText.setTextColor(ContextCompat.getColor(context, R.color.tweet_text))
        }
        val textColor = tweetText.currentTextColor
        if (s.length > maxTweetLength) {
            submitButton.visibility = View.INVISIBLE
            tweetText.setTextColor(ContextCompat.getColor(context, R.color.tweet_text_away))
        } else {
            submitButton.visibility = View.VISIBLE
            tweetText.setTextColor(textColor)
        }
    }

    override fun afterTextChanged(s: Editable) {
        //
    }

}
