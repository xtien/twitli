/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.twitli.android.twitter.BuildConfig
import com.twitli.android.twitter.MyApplication
import com.twitli.android.twitter.R
import com.twitli.android.twitter.tweet.TwitManager
import com.twitli.android.twitter.wiki.WikiPageManager
import javax.inject.Inject

class SettingsFragment : Fragment() {

    var settingsViewModel: SettingsViewModel? = null
    private var versionView: TextView? = null

    @Inject
    lateinit var wikiPageManager: WikiPageManager

    @Inject
    lateinit var twitManager: TwitManager

    private var activeSwitch: Switch? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        activeSwitch = view.findViewById(R.id.active_switch)
        (activeSwitch as Switch).setOnCheckedChangeListener({ buttonView: CompoundButton?, isChecked: Boolean -> settingsViewModel!!.setActive(isChecked) })
        versionView = view.findViewById(R.id.version)
        val intervalSpinner = view.findViewById<Spinner>(R.id.interval_spinner)
        intervalSpinner.adapter = ArrayAdapter.createFromResource(activity!!, R.array.tweet_interval, android.R.layout.simple_spinner_item)
        intervalSpinner.setSelection(prefs.getInt("tweet_interval", 0))
        intervalSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val editor = activity!!.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
                editor.putInt("tweet_interval", position)
                editor.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel!!.isActive().observeForever { b: Boolean? ->
            if (b != null && activeSwitch!!.isChecked != b) {
                activeSwitch!!.isChecked = b
            }
        }
        versionView!!.text = BuildConfig.VERSION_NAME
        (activity!!.applicationContext as MyApplication).appComponent?.inject(this)
        Log.d(LOGTAG, if ("wikiPageManager $wikiPageManager" == null) "null" else "not null")
    }

    companion object {
        private val LOGTAG = SettingsFragment::class.java.simpleName
        fun newInstance(): Fragment {
            return SettingsFragment()
        }
    }
}
