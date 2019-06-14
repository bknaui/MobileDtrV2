package com.dohro7.mobiledtrv2.repository.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitApi retrofitApi;
    //private static final String BASE_URL = "http://203.177.67.122";
    private static final String BASE_URL = "http://192.168.100.66:8000";

    public static RetrofitApi getRetrofitApi(Context context) {
        if (retrofitApi == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(new NullConverter())
                    .build();

            retrofitApi = retrofit.create(RetrofitApi.class);
        }

        return retrofitApi;
    }
}
