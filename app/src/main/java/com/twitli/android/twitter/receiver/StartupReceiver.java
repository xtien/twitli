/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package com.twitli.android.twitter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.twitli.android.twitter.service.TwitService;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent icycle = new Intent("nl.christine.twitfollowers.servicve.TwitService");
            icycle.setClass(context, TwitService.class);
            context.startService(icycle);
        }
    }
}
