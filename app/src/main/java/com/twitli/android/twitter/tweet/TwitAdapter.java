/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.tweet;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.twitli.android.twitter.R;
import com.twitli.android.twitter.data.BaseViewHolder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.SimpleDateFormat;
import java.util.*;

public class TwitAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private SimpleDateFormat sFormat;
    List<Tweet> tweets = new ArrayList<>();
    FastDateFormat format;
    private Context context;
    private OnLikeClickListener onLikeClickListener;
    private OnReplyClickListener onReplyClickListener;

    public interface OnLikeClickListener {
        void onLikeClicked(Long tweetId);
    }
    public interface OnReplyClickListener {
        void onReplyClicked(Long tweetId);
    }

    public void setOnLikeClickListener(OnLikeClickListener listener) {
        this.onLikeClickListener = listener;
    }
    public void setOnReplyClickListener(OnReplyClickListener listener) {
        this.onReplyClickListener = listener;
    }

    public TwitAdapter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            format = FastDateFormat.getInstance("MM-dd kk:mm", TimeZone.getDefault(), Locale.getDefault());
        } else {
            sFormat = new SimpleDateFormat("MM-dd kk:mm");
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        return new TwitAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets.addAll(tweets);
    }

    private class ViewHolder extends BaseViewHolder {

        TextView twitterIdView;
        TextView timeView;
        TextView nameView;
        TextView tweetTextView;
        TextView like;
        TextView reply;

        public ViewHolder(View view) {
            super(view);
            twitterIdView = view.findViewById(R.id.twitter_id);
            timeView = view.findViewById(R.id.time);
            nameView = view.findViewById(R.id.twitter_name);
            tweetTextView = view.findViewById(R.id.tweet_text);
            like = view.findViewById(R.id.icon_like);
            reply = view.findViewById(R.id.icon_reply);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Tweet tweet = tweets.get(position);
            twitterIdView.setText(tweet.getScreenName());
            if (NumberUtils.isCreatable(tweet.getTime())) {
                Date date = new Date(Long.parseLong(tweet.getTime()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    timeView.setText(format.format(date));
                } else {
                    timeView.setText(sFormat.format(date));
                }
            }
            nameView.setText(String.valueOf(tweet.getName()));
            tweetTextView.setText(String.valueOf(tweet.getText()));
            if (tweet.getText().length() == 280) {
                tweetTextView.setTextColor(ContextCompat.getColor(context, R.color.tweet_highlight));
            } else {
                tweetTextView.setTextColor(ContextCompat.getColor(context, R.color.tweet_text));
            }

            like.setOnClickListener(v -> {
                onLikeClickListener.onLikeClicked(tweet.getTweetId());
            });
            reply.setOnClickListener(v -> {
                onReplyClickListener.onReplyClicked(tweet.getTweetId());
            });
        }

        @Override
        protected void clear() {
            twitterIdView.setText("");
            timeView.setText("");
            nameView.setText("");
            tweetTextView.setText("");
        }
    }

}
