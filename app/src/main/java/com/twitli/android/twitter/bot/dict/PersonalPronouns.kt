/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.bot.dict

import com.twitli.android.twitter.bot.dict.type.Gender
import com.twitli.android.twitter.bot.dict.type.PersonalPronoun

class PersonalPronouns  {

    companion object{

        fun populateData(): Array<PersonalPronoun> {
            return arrayOf(

                    PersonalPronoun("i", 1,1,false),
                    PersonalPronoun("my", 1,2,false),
                    PersonalPronoun("me", 1,3,false),
                    PersonalPronoun("me", 1,4,false),
                    PersonalPronoun("we", 1,1,true),
                    PersonalPronoun("our", 1,2,true),
                    PersonalPronoun("us", 1,3,true),
                    PersonalPronoun("us", 1,4,true),

                    PersonalPronoun("you", 2,1,false),
                    PersonalPronoun("your", 2,2,false),
                    PersonalPronoun("you", 2,3,false),
                    PersonalPronoun("you", 2,4,false),
                    PersonalPronoun("you", 2,1,true),
                    PersonalPronoun("your", 2,2,true),
                    PersonalPronoun("you", 2,3,true),
                    PersonalPronoun("you", 2,4,true),

                    PersonalPronoun("she", 3,1,false, Gender.F),
                    PersonalPronoun("her", 3,2,false, Gender.F),
                    PersonalPronoun("her", 3,3,false, Gender.F),
                    PersonalPronoun("her", 3,4,false, Gender.F),
                    PersonalPronoun("he", 3,1,false, Gender.M),
                    PersonalPronoun("his", 3,2,false, Gender.M),
                    PersonalPronoun("him", 3,3,false, Gender.M),
                    PersonalPronoun("him", 3,4,false, Gender.M),
                    PersonalPronoun("she", 3,1,true),
                    PersonalPronoun("her", 3,2,true),
                    PersonalPronoun("her", 3,3,true),
                    PersonalPronoun("her", 3,4,true)
            )
        }
    }

}
