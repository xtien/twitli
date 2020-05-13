package com.twitli.android.twitter.tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.R;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;

public class TwitFragment extends Fragment {

    private TwitViewModel twitViewModel;
    private RecyclerView listView;
    private TwitAdapter adapter;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton button;
    private LinearLayout tweetView;
    private Button submitButton;

    @Inject
    public TwitManager twitManager;

    private EditText editText;
    private boolean isTweeting = false;

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
        tweetView = view.findViewById(R.id.tweet);
        editText = view.findViewById(R.id.tweet_text);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MyApplication) getActivity().getApplicationContext()).appComponent.inject(this);

        isTweeting = false;

        twitViewModel = new ViewModelProvider(this).get(TwitViewModel.class);
        twitViewModel.getTweets().observe(getActivity(), contacts -> {
            adapter.setTweets(contacts);
            adapter.notifyDataSetChanged();
        });

        adapter = new TwitAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);

        button.setOnClickListener(v -> {
            if (!isTweeting) {
                listView.setAlpha(0.5f);
                tweetView.setVisibility(View.VISIBLE);
                isTweeting = true;
            } else {
                listView.setAlpha(1.0f);
                tweetView.setVisibility(View.INVISIBLE);
                isTweeting = false;
            }
        });

        submitButton.setOnClickListener(v -> {
            listView.setAlpha(1.0f);
            tweetView.setVisibility(View.INVISIBLE);
            hideKeyboard(getActivity());
            if (editText.getText() != null && editText.getText().toString() != null) {
                String message = editText.getText().toString();
                if (NumberUtils.isCreatable(message)) {
                    Intent icycle = new Intent();
                    icycle.setAction("nl.christine.app.message");
                    icycle.putExtra("message", message);
                    getActivity().sendBroadcast(icycle);

                    twitManager.tweet(editText.getText().toString());
                    editText.setText("");
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}