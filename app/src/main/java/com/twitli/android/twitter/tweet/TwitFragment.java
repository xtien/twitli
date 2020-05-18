package com.twitli.android.twitter.tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.R;
import com.twitli.android.twitter.dagger.TwitViewModelFactory;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitFragment extends Fragment implements TwitAdapter.OnLikeClickListener, TwitAdapter.OnReplyClickListener {

    private TwitViewModel twitViewModel;
    private RecyclerView listView;
    private TwitAdapter adapter;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton button;
    private LinearLayout tweetView;
    private Button submitButton;
    private LinearLayout replyView;
    private Button replyButton;
    ExecutorService es = Executors.newCachedThreadPool();

    @Inject
    public TwitManager twitManager;

    private EditText tweetText;
    private boolean isTweeting = false;
    private TextView textLengthView;
    private Map<Long, Tweet> tweets = new HashMap<>();
    private EditText replyText;
    private TextView replyLengthView;
    private long tweetLoadTime = 0l;
    private SwipeRefreshLayout swipeContainer;

    public static Fragment newInstance() {
        return new TwitFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.twit_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = view.findViewById(R.id.listview);
        button = view.findViewById(R.id.tweet_button);
        submitButton = view.findViewById(R.id.submit_tweet);
        replyButton = view.findViewById(R.id.submit_reply);
        tweetView = view.findViewById(R.id.tweet);
        replyView = view.findViewById(R.id.reply);
        tweetText = view.findViewById(R.id.status_text);
        replyText = view.findViewById(R.id.reply_text);
        textLengthView = view.findViewById(R.id.text_length);
        replyLengthView = view.findViewById(R.id.reply_text_length);
        swipeContainer = view.findViewById(R.id.swiperefresh);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MyApplication) getActivity().getApplicationContext()).appComponent.inject(this);

        isTweeting = false;

        TwitViewModelFactory factory = new TwitViewModelFactory(getActivity().getApplication(), twitManager);

        twitViewModel = new ViewModelProvider(this, factory).get(TwitViewModel.class);
        twitViewModel.getTweets().observe(getActivity(), tweets -> {
            for(Tweet tweet : tweets){
                this.tweets.put(tweet.getTweetId(), tweet);
            }

            adapter.setTweets(new ArrayList<>(this.tweets.values()));
            adapter.notifyDataSetChanged();
         });

        adapter = new TwitAdapter();
        adapter.setOnLikeClickListener(this);
        adapter.setOnReplyClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);

        TextWatcher tw = new MyTextWatcher(getContext(), tweetText, textLengthView, submitButton);
        TextWatcher tw2 = new MyTextWatcher(getContext(), replyText, replyLengthView, replyButton);

        tweetText.addTextChangedListener(tw);
        replyText.addTextChangedListener(tw2);

        button.setOnClickListener(v -> {
            if (!isTweeting) {
                listView.setAlpha(0.5f);
                tweetView.setVisibility(View.VISIBLE);
                isTweeting = true;
            } else {
                listView.setAlpha(1.0f);
                tweetView.setVisibility(View.INVISIBLE);
                replyView.setVisibility(View.INVISIBLE);
                isTweeting = false;
            }
        });

        swipeContainer.setOnRefreshListener(() -> es.execute(() -> {
            twitViewModel.loadTweets();
            swipeContainer.setRefreshing(false);
        }));

        submitButton.setOnClickListener(v -> doEdit(tweetView, tweetText));

//        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//        if(prefs.getLong("last_tweets_loaded", 0l) < System.currentTimeMillis() - tweetLoadTime){
//            es.execute(() -> {
//                twitViewModel.loadTweets();
//            });
//            Editor editor = prefs.edit();
//            editor.putLong("last_tweet_load", System.currentTimeMillis());
//            editor.apply();
//        }
    }

    private void doEdit(LinearLayout view, TextView textview){
        doEdit(view, textview, null);
    }

    private void doEdit(LinearLayout replyView, TextView replyText, Long tweetId) {

        listView.setAlpha(1.0f);
        replyView.setVisibility(View.INVISIBLE);
        hideKeyboard(getActivity());

        if (replyText.getText() != null && replyText.getText().toString() != null) {
            String message = replyText.getText().toString();

            String[] words = message.split(" ");
            for (String word : words) {
                word = word.trim();
                if (word != null && word.length() > 2 && NumberUtils.isCreatable(word)) {
                    Intent icycle = new Intent();
                    icycle.setAction("nl.christine.app.message");
                    icycle.putExtra("message", word);
                    getActivity().sendBroadcast(icycle);
                }
            }

            if(tweetId == null){
                twitManager.tweet(replyText.getText().toString());
            } else {
                twitManager.reply(replyText.getText().toString(), tweetId);
            }
            replyText.setText("");
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onLikeClicked(Long tweetId) {
        es.execute(() -> twitManager.like(tweetId));
    }

    @Override
    public void onReplyClicked(Long tweetId) {
        listView.setAlpha(0.5f);
        replyView.setVisibility(View.VISIBLE);
        replyButton.setOnClickListener(v ->  doEdit(replyView, replyText, tweetId));
    }
}
