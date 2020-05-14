package com.twitli.android.twitter.tweet;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.twitli.android.twitter.R;

public class MyTextWatcher implements TextWatcher {

    private final EditText tweetText;
    private final TextView textLengthView;
    private final Context context;
    private final Button submitButton;

    private int maxTweetLength = 280;

    public MyTextWatcher(Context context, EditText tweetText, TextView textLengthView, Button submitButton) {
        this.tweetText = tweetText;
        this.textLengthView = textLengthView;
        this.context = context;
        this.submitButton = submitButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textLengthView.setText(Integer.toString(s.length()));
        if (s.length() >= maxTweetLength) {
            textLengthView.setTextColor(ContextCompat.getColor(context, R.color.text_count_full));
            tweetText.setTextColor(ContextCompat.getColor(context, R.color.tweet_highlight));
        } else {
            textLengthView.setTextColor(ContextCompat.getColor(context, R.color.text_count));
            tweetText.setTextColor(ContextCompat.getColor(context, R.color.tweet_text));
        }
        int textColor = tweetText.getCurrentTextColor();
        if (s.length() > maxTweetLength) {
            submitButton.setVisibility(View.INVISIBLE);
            tweetText.setTextColor(ContextCompat.getColor(context, R.color.tweet_text_away));
        } else {
            submitButton.setVisibility(View.VISIBLE);
            tweetText.setTextColor(textColor);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        //
    }
}
