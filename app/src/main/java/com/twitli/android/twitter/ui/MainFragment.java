/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.twitli.android.twitter.MyApplication;
import com.twitli.android.twitter.R;
import com.twitli.android.twitter.tweet.TwitManager;
import com.twitli.android.twitter.wiki.WikiPageManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import java.io.IOException;

public class MainFragment extends Fragment {

    private static final String LOGTAG = MainFragment.class.getSimpleName();
    SettingsViewModel settingsViewModel;

    @Inject
    public WikiPageManager wikiPageManager;

    @Inject
    public TwitManager twitManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        ((MyApplication) getActivity().getApplicationContext()).appComponent.inject(this);
        Log.d(LOGTAG, "wikiPageManager " + wikiPageManager == null ? "null" : "not null");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((Switch) view.findViewById(R.id.active_switch)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setActive(isChecked);
        });

        EditText editText = view.findViewById(R.id.tweet_text);
        Button button = view.findViewById(R.id.submit);
        button.setOnClickListener(v -> {
            String message = editText.getText().toString();
            if (NumberUtils.isCreatable(message)) {
                Intent icycle = new Intent();
                icycle.setAction("nl.christine.app.message");
                icycle.putExtra("message", message);
                getActivity().sendBroadcast(icycle);
            }
            twitManager.tweet(message);
            editText.setText("");
        });
    }
}
