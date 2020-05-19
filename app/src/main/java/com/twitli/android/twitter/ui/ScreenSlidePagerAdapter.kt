/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class ScreenSlidePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    private val fragments: MutableList<Fragment>? = ArrayList()
    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getCount(): Int {
        return if (fragments == null || fragments.isEmpty()) 0 else fragments.size
    }

    fun addFragment(fragment: Fragment) {
        fragments!!.add(fragment)
    }
}
