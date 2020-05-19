/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */
package com.twitli.android.twitter.data

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var currentPosition = 0
        private set

    protected abstract fun clear()
    open fun onBind(position: Int) {
        currentPosition = position
        clear()
    }

}
