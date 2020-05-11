/*
 * Copyright (c) 2020, Zaphod Consulting BV, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www.apache.org/licenses/LICENSE-2.0.
 */

package nl.christine.twitfollowers.wiki;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.christine.twitfollowers.MyApplication;
import nl.christine.twitfollowers.data.ContentRepository;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.io.IOException;

public class WikiController implements Callback<ResponseBody> {

    private static final String LOGTAG = WikiController.class.getSimpleName();
    private String baseUrl = "https://nl.wikipedia.org/";

    private ContentRepository repository;

    public WikiPageManager wikiPageManager;

    public WikiController(WikiPageManager wikiPageManager){
        this.wikiPageManager = wikiPageManager;
    }

    public void start(String year) {

        repository = new ContentRepository(MyApplication.getApplication());

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WikiApi wikiApi = retrofit.create(WikiApi.class);

        Call<ResponseBody> call = wikiApi.getPage("1820");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if(response.isSuccessful()){
            ResponseBody body = response.body();
            try {
                String string = body.string();
                wikiPageManager.analyzePage(string);
                Log.d(LOGTAG, "page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d(LOGTAG, t.getMessage());

    }
}
