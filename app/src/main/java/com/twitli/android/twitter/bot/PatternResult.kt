/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot

import com.twitli.android.twitter.bot.dict.Pattern
import com.twitli.android.twitter.bot.dict.type.Word

class PatternResult(pat: Pattern?, resultList: List<Word>) {
    val pattern = pat
    val words = resultList
}
